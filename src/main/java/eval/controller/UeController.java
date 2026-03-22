package eval.controller;

import eval.dto.EnseignementDTO;
import eval.dto.UeDTO;
import eval.service.UeImportService;
import eval.service.UeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ue")
public class UeController {

    @Autowired
    private UeImportService ueImportService;
    @Autowired
    private UeService ueService;

    @PostMapping("/import")
    public ResponseEntity<String> importUes(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }
        try {
            ueImportService.importFromExcel(file);
            return ResponseEntity.ok("Import réussi !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/etudiant")
    public ResponseEntity<List<UeDTO>> getUesByEtudiant(
            @RequestParam("mail") String mail,
            @RequestParam("semestre") String nomSemestre) {
        List<UeDTO> ues = ueService.getUesByEtudiantMailAndSemestre(mail, nomSemestre);
        if (ues.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ues);
    }

    @GetMapping("/{idUe}/enseignements")
    public ResponseEntity<List<EnseignementDTO>> getEnseignements(@PathVariable String idUe) {
        try {
            List<EnseignementDTO> enseignements = ueService.getEnseignementsByUe(idUe);
            if (enseignements.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(enseignements);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}