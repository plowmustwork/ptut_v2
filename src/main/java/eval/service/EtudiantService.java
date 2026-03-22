package eval.service;
import eval.dto.EtudiantDTO;
import eval.entity.Etudiant;
import eval.repository.EtudiantRepository;
import eval.utility.EtudiantMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eval.repository.PromotionRepository;


@Service
public class EtudiantService {
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private EtudiantMapper etudiantMapper;


    public EtudiantDTO trouverEtudiant(Long id){
        Etudiant etudiant = etudiantRepository.findById(id);
        return etudiantMapper.toDto(etudiant);
    }

    public EtudiantDTO ajouterEtudiant(EtudiantDTO etudiantDTO){
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(etudiantDTO.getNom());
        etudiant.setPrenom(etudiantDTO.getPrenom());
        etudiant.setMail(etudiantDTO.getMail());
        etudiant.setLv2(etudiantDTO.getLv2());
        lierEtudiantAPromotionParNom(etudiant, etudiantDTO.getPromotionNom());
        if (etudiant.getPromotion() == null) {
            lierEtudiantAPromotionParId(etudiant, etudiantDTO.getPromotionId());
        }

        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(savedEtudiant);
    }

    public void importEtudiantsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Colonnes : 0=id, 1=mail, 2=nom, 3=prenom, 4=nom_promotion, 5=lv2
                Cell mailCell = row.getCell(1);
                Cell nomCell  = row.getCell(2);
                Cell prenomCell = row.getCell(3);
                if (mailCell == null || nomCell == null || prenomCell == null) continue;

                Etudiant etudiant = new Etudiant();
                etudiant.setMail(mailCell.getStringCellValue());
                etudiant.setNom(nomCell.getStringCellValue());
                etudiant.setPrenom(prenomCell.getStringCellValue());

                // nom_promotion (colonne 4) — texte
                Cell promotionCell = row.getCell(4);
                if (promotionCell != null) {
                    if (promotionCell.getCellType() == CellType.STRING) {
                        lierEtudiantAPromotionParNom(etudiant, promotionCell.getStringCellValue());
                    } else if (promotionCell.getCellType() == CellType.NUMERIC) {
                        // Compatibilite ancienne version (promotion_id)
                        Long promotionId = (long) promotionCell.getNumericCellValue();
                        lierEtudiantAPromotionParId(etudiant, promotionId);
                    }
                }

                // lv2 (colonne 5)
                Cell lv2Cell = row.getCell(5);
                if (lv2Cell != null) {
                    etudiant.setLv2(lv2Cell.getStringCellValue());
                }

                etudiantRepository.save(etudiant);
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
        if (etudiantOpt.isEmpty()) {
            return null;
        }

        return etudiantMapper.toDto(etudiantOpt.get());
    }

    private void lierEtudiantAPromotionParNom(Etudiant etudiant, String nomPromotion) {
        if (nomPromotion == null || nomPromotion.isBlank()) {
            return;
        }

        promotionRepository.findByNomPromotion(nomPromotion.trim())
            .ifPresent(promotion -> ajouterEtudiantDansPromotion(etudiant, promotion));
    }

    private void lierEtudiantAPromotionParId(Etudiant etudiant, Long promotionId) {
        if (promotionId == null) {
            return;
        }

        promotionRepository.findById(promotionId)
            .ifPresent(promotion -> ajouterEtudiantDansPromotion(etudiant, promotion));
    }

    private void ajouterEtudiantDansPromotion(Etudiant etudiant, eval.entity.Promotion promotion) {
        etudiant.setPromotion(promotion);

        if (promotion.getListeEtudiants() == null) {
            promotion.setListeEtudiants(new ArrayList<>());
        }

        boolean dejaPresent = promotion.getListeEtudiants().stream()
            .anyMatch(e -> e.getMail() != null && e.getMail().equalsIgnoreCase(etudiant.getMail()));

        if (!dejaPresent) {
            promotion.getListeEtudiants().add(etudiant);
        }
    }
}