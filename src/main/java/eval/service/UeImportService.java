package eval.service;

import eval.entity.Enseignement;
import eval.entity.Semestre;
import eval.entity.Ue;
import eval.repository.EnseignementRepository;
import eval.repository.SemestreRepository;
import eval.repository.UeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class UeImportService {

    @Autowired
    private UeRepository ueRepository;
    @Autowired
    private EnseignementRepository enseignementRepository;
    @Autowired
    private SemestreRepository semestreRepository;

    public void importFromExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Parse row 1 — "Promotion : FIE3  |  Semestre : S6  |  Année : 2025-2026"
            String infoCell = sheet.getRow(0).getCell(0).getStringCellValue();
            String[] infoParts = infoCell.split("\\|");  // renamed to infoParts ✅
            String nomPromotion = infoParts[0].replace("Promotion :", "").trim();
            String nomSemestre  = infoParts[1].replace("Semestre :", "").trim();
            String anneeDebut   = infoParts[2].replace("Année :", "").trim().split("-")[0].trim();

            Semestre semestre = semestreRepository
                .findByNomSemestreAndPromotionNomPromotionAndAnneeAnneeDebut(
                    nomSemestre, nomPromotion, anneeDebut)
                .orElseThrow(() -> new RuntimeException(
                    "Semestre introuvable: " + nomSemestre + " / " + nomPromotion + " / " + anneeDebut));

            // Start at row 4 (index 3) — rows 1,2=info, 3=headers
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell idUeCell          = row.getCell(0);
                Cell intituleCell      = row.getCell(1);
                Cell enseignementsCell = row.getCell(2);

                if (idUeCell == null || intituleCell == null) continue;

                String idUE     = idUeCell.getStringCellValue().trim();
                String intitule = intituleCell.getStringCellValue().trim();
                if (idUE.isBlank()) continue;

                // Create or update UE
                Ue ue = ueRepository.findByIdUE(idUE).orElse(new Ue());
                ue.setIdUE(idUE);
                ue.setIntitule(intitule);
                ue.setSemestre(semestre);
                ueRepository.save(ue);

                // Parse enseignements — format: "code|libelle;code|libelle"
                if (enseignementsCell == null) continue;
                String enseignementsRaw = enseignementsCell.getStringCellValue().trim();
                if (enseignementsRaw.isBlank()) continue;

                String[] enseignementsParts = enseignementsRaw.split(";");  // renamed ✅
                for (String part : enseignementsParts) {
                    part = part.trim();
                    if (!part.contains("|")) continue;

                    String[] split  = part.split("\\|", 2);
                    String code     = split[0].trim();
                    String libelle  = split[1].trim();

                    // Avoid duplicates by code
                    boolean exists = ue.getEnseignements().stream()
                        .anyMatch(e -> e.getCode().equalsIgnoreCase(code));
                    if (exists) continue;

                    Enseignement enseignement = new Enseignement();
                    enseignement.setCode(code);
                    enseignement.setLibelle(libelle);
                    enseignement.setUe(ue);
                    enseignementRepository.save(enseignement);
                }
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'import du fichier Excel", e);
        }
    }
}