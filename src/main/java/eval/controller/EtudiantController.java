package eval.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import eval.dto.EtudiantDTO;
import eval.service.EtudiantService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping("/trouver/{id}")                          // ✅ Fix 1: was @RequestMapping
    public ResponseEntity<EtudiantDTO> trouverEtudiant(@PathVariable Long id) {
        EtudiantDTO etudiant = etudiantService.trouverEtudiant(id);
        if (etudiant == null) {                           // ✅ Fix 2: null check
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(etudiant);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<EtudiantDTO> ajouterEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
        return ResponseEntity.ok(
            etudiantService.ajouterEtudiant(etudiantDTO)
        );
    }

    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        etudiantService.importEtudiantsFromExcel(file);
        return ResponseEntity.ok("Importation réussie !");
    }

    @GetMapping("/rechercher")
    public ResponseEntity<List<EtudiantDTO>> rechercherParPrenom(@RequestParam String prenom) {
        List<EtudiantDTO> resultats = etudiantService.rechercherParPrenom(prenom);
        if (resultats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/mail")
    public ResponseEntity<EtudiantDTO> rechercherParMail(@RequestParam String mail) {
        EtudiantDTO etudiant = etudiantService.rechercherParMail(mail);
        if (etudiant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(etudiant);
    }
}