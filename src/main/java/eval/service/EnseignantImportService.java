package eval.service;

import eval.entity.Enseignant;
import eval.entity.Enseignement;
import eval.repository.EnseignantRepository;
import eval.repository.EnseignementRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EnseignantImportService {

    private final EnseignantRepository enseignantRepository;
    private final EnseignementRepository enseignementRepository;

    public EnseignantImportService(EnseignantRepository enseignantRepository,
                                   EnseignementRepository enseignementRepository) {
        this.enseignantRepository = enseignantRepository;
        this.enseignementRepository = enseignementRepository;
    }

    public void importFromExcel(MultipartFile file) throws IOException {
    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String mail   = getCellValue(row, 0);
            String nom    = getCellValue(row, 1);
            String prenom = getCellValue(row, 2);
            String codesRaw = getCellValue(row, 3); // colonne D maintenant

            Enseignant enseignant = new Enseignant();
            enseignant.setMail(mail);
            enseignant.setNom(nom);
            enseignant.setPrenom(prenom);

            List<Enseignement> enseignements = new ArrayList<>();
            if (codesRaw != null && !codesRaw.isBlank()) {
                Arrays.stream(codesRaw.split(","))
                      .map(String::trim)
                      .filter(code -> !code.isEmpty())
                      .forEach(code -> enseignementRepository
                          .findByCode(code)
                          .ifPresentOrElse(
                              enseignements::add,
                              () -> System.err.println("Code inconnu ignoré : " + code)
                          ));
            }
            enseignant.setEnseignements(enseignements);
            enseignantRepository.save(enseignant);
        }
    }
}
    

    private String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default      -> null;
        };
    }
}