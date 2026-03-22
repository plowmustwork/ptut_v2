package eval.controller;

import eval.service.UeImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ue")
public class UeImportController {

    @Autowired
    private UeImportService ueImportService;

    @PostMapping("/import")
    public ResponseEntity<String> importUes(
            @RequestParam("file") MultipartFile file,
            @RequestParam("promotion") String nomPromotion,
            @RequestParam("semestre") String nomSemestre,
            @RequestParam("anneeDebut") String anneeDebut) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        ueImportService.importFromExcel(file, nomPromotion, nomSemestre, anneeDebut);
        return ResponseEntity.ok("Import réussi ✅");
    }
}