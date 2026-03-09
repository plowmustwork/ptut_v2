package eval.service;
import eval.dto.EtudiantDTO;
import eval.entity.Etudiant;
import eval.entity.Promotion;
import eval.repository.EtudiantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import eval.repository.PromotionRepository;
import java.util.Optional;

@Service
public class EtudiantService {
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private PromotionRepository promotionRepository;

    public EtudiantDTO trouverEtudiant(Long id){
        Etudiant etudiant = etudiantRepository.findById(id);
        EtudiantDTO etudiantDTO = new EtudiantDTO();
        etudiantDTO.EtudiantMapper(etudiant);
        return etudiantDTO;
    }

    public EtudiantDTO ajouterEtudiant(EtudiantDTO etudiantDTO){
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(etudiantDTO.getNom());
        etudiant.setPrenom(etudiantDTO.getPrenom());
        etudiant.setMail(etudiantDTO.getMail());
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
                Cell cell = row.getCell(3);

                if (cell == null) {
                    continue; 
                }

                String promotionNom = cell.getStringCellValue();
                Promotion promotion = promotionRepository
                    .findByNomPromotion(promotionNom)
                    .orElseGet(() -> {
                        Promotion p = new Promotion();
                        p.setNomPromotion(promotionNom);
                        return promotionRepository.save(p);
                    });
                Etudiant etudiant = new Etudiant();
    
                etudiant.setNom(row.getCell(0).getStringCellValue());
                etudiant.setPrenom(row.getCell(1).getStringCellValue());
                etudiant.setMail(row.getCell(2).getStringCellValue());
                etudiant.setPromotion(promotion);
                etudiantRepository.save(etudiant);
            }
    
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation du fichier Excel", e);
        }
    }

}
