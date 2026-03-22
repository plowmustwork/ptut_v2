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
        if (etudiantDTO.getPromotionId() != null) {
            promotionRepository.findById(etudiantDTO.getPromotionId())
                .ifPresent(etudiant::setPromotion);
        }
        etudiantRepository.save(etudiant);
        return etudiantDTO;
    }

    public void importEtudiantsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Colonnes : 0=id, 1=mail, 2=nom, 3=prenom, 4=promotion_id, 5=lv2
                Cell mailCell = row.getCell(1);
                Cell nomCell  = row.getCell(2);
                Cell prenomCell = row.getCell(3);
                if (mailCell == null || nomCell == null || prenomCell == null) continue;

                Etudiant etudiant = new Etudiant();
                etudiant.setMail(mailCell.getStringCellValue());
                etudiant.setNom(nomCell.getStringCellValue());
                etudiant.setPrenom(prenomCell.getStringCellValue());

                // promotion_id (colonne 4) — numérique
                Cell promotionCell = row.getCell(4);
                if (promotionCell != null && promotionCell.getCellType() == CellType.NUMERIC) {
                    Long promotionId = (long) promotionCell.getNumericCellValue();
                    promotionRepository.findById(promotionId)
                        .ifPresent(etudiant::setPromotion);
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
}