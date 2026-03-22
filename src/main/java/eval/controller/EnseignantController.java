package eval.controller;

import eval.service.EnseignantImportService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {

    private final EnseignantImportService enseignantImportService;

    public EnseignantController(EnseignantImportService enseignantImportService) {
        this.enseignantImportService = enseignantImportService;
    }

    @Operation(summary = "Import enseignants from Excel file")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importEnseignants(
            @RequestPart("file") MultipartFile file) {
        try {
            enseignantImportService.importFromExcel(file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fichier invalide : " + e.getMessage());
        }
    }
}