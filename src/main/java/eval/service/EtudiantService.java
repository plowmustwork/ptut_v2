package eval.service;

import eval.dto.EtudiantDTO;
import eval.entity.Annee;
import eval.entity.Etudiant;
import eval.entity.InscriptionPromotion;
import eval.repository.AnneeRepository;
import eval.repository.EtudiantRepository;
import eval.repository.InscriptionPromotionRepository;
import eval.repository.PromotionRepository;
import eval.utility.EtudiantMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private AnneeRepository anneeRepository;
    @Autowired
    private InscriptionPromotionRepository inscriptionPromotionRepository;
    @Autowired
    private EtudiantMapper etudiantMapper;


    public EtudiantDTO trouverEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id);
        return etudiantMapper.toDto(etudiant);
    }

    public EtudiantDTO ajouterEtudiant(EtudiantDTO etudiantDTO) {
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(etudiantDTO.getNom());
        etudiant.setPrenom(etudiantDTO.getPrenom());
        etudiant.setMail(etudiantDTO.getMail());
        etudiant.setLv2(etudiantDTO.getLv2());

        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        // Try by promotion name first, then by ID
        boolean linked = lierEtudiantAPromotionParNom(savedEtudiant, etudiantDTO.getPromotionNom(), etudiantDTO.getAnneeDebut());
        if (!linked) {
            lierEtudiantAPromotionParId(savedEtudiant, etudiantDTO.getPromotionId(), etudiantDTO.getAnneeDebut());
        }

        return etudiantMapper.toDto(savedEtudiant);
    }

    public void importEtudiantsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Colonnes: 0=id, 1=mail, 2=nom, 3=prenom, 4=nom_promotion, 5=lv2, 6=annee_debut
                Cell mailCell   = row.getCell(1);
                Cell nomCell    = row.getCell(2);
                Cell prenomCell = row.getCell(3);
                if (mailCell == null || nomCell == null || prenomCell == null) continue;

                Etudiant etudiant = new Etudiant();
                etudiant.setMail(mailCell.getStringCellValue());
                etudiant.setNom(nomCell.getStringCellValue());
                etudiant.setPrenom(prenomCell.getStringCellValue());

                // lv2 (colonne 5)
                Cell lv2Cell = row.getCell(5);
                if (lv2Cell != null) {
                    etudiant.setLv2(lv2Cell.getStringCellValue());
                }

                Etudiant savedEtudiant = etudiantRepository.save(etudiant);

                // annee_debut (colonne 6)
                String anneeDebut = null;
                Cell anneeCell = row.getCell(6);
                if (anneeCell != null) {
                    if (anneeCell.getCellType() == CellType.STRING) {
                        anneeDebut = anneeCell.getStringCellValue().trim();
                    } else if (anneeCell.getCellType() == CellType.NUMERIC) {
                        anneeDebut = String.valueOf((long) anneeCell.getNumericCellValue()); // converts 2025.0 → "2025" ✅
                    }
                }

                // nom_promotion (colonne 4)
                Cell promotionCell = row.getCell(4);
                if (promotionCell != null) {
                    if (promotionCell.getCellType() == CellType.STRING) {
                        lierEtudiantAPromotionParNom(savedEtudiant, promotionCell.getStringCellValue(), anneeDebut);
                    } else if (promotionCell.getCellType() == CellType.NUMERIC) {
                        Long promotionId = (long) promotionCell.getNumericCellValue();
                        lierEtudiantAPromotionParId(savedEtudiant, promotionId, anneeDebut);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation du fichier Excel", e);
        }
    }

    public List<EtudiantDTO> rechercherParPrenom(String prenom) {
        return etudiantRepository.findByPrenomContainingIgnoreCase(prenom)
                .stream()
                .map(etudiantMapper::toDto)
                .collect(Collectors.toList());
    }

    public EtudiantDTO rechercherParMail(String mail) {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByMailIgnoreCase(mail);
        return etudiantOpt.map(etudiantMapper::toDto).orElse(null);
    }

    // ---- private helpers ----

    private boolean lierEtudiantAPromotionParNom(Etudiant etudiant, String nomPromotion, String anneeDebut) {
        if (nomPromotion == null || nomPromotion.isBlank()) return false;

        Optional<eval.entity.Promotion> promotionOpt = promotionRepository.findByNomPromotion(nomPromotion.trim());
        if (promotionOpt.isEmpty()) return false;

        creerInscription(etudiant, promotionOpt.get().getId(), anneeDebut);
        return true;
    }

    private void lierEtudiantAPromotionParId(Etudiant etudiant, Long promotionId, String anneeDebut) {
        if (promotionId == null) return;

        promotionRepository.findById(promotionId)
                .ifPresent(promotion -> creerInscription(etudiant, promotion.getId(), anneeDebut));
    }

    private void creerInscription(Etudiant etudiant, Long promotionId, String anneeDebut) {
    // Avoid duplicates per etudiant + promotion + annee
        Optional<Annee> anneeOpt = Optional.empty();
        if (anneeDebut != null && !anneeDebut.isBlank()) {
            anneeOpt = anneeRepository.findByAnneeDebut(anneeDebut.trim());
            if (anneeOpt.isEmpty()) {
                System.err.println("Annee not found for anneeDebut: " + anneeDebut); // helps debug ✅
                return; // don't create inscription without a valid annee
            }
        }

        boolean dejaPresent = inscriptionPromotionRepository
                .existsByEtudiantIdAndPromotionId(etudiant.getId(), promotionId);
        if (dejaPresent) return;

        InscriptionPromotion inscription = new InscriptionPromotion();
        inscription.setEtudiant(etudiant);

        promotionRepository.findById(promotionId)
                .ifPresent(inscription::setPromotion);

        anneeOpt.ifPresent(inscription::setAnnee); // ✅ set the found annee

        inscriptionPromotionRepository.save(inscription);
    }
}