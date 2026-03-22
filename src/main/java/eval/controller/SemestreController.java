package eval.controller;

import eval.dto.SemestreDTO;
import eval.service.SemestreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semestres")
public class SemestreController {

    @Autowired
    private SemestreService semestreService;

    @GetMapping
    public ResponseEntity<?> getSemestres(
            @RequestParam("promotion") String nomPromotion,
            @RequestParam("anneeDebut") String anneeDebut) {
        try {
            List<SemestreDTO> semestres = semestreService
                .getSemestresByPromotionAndAnnee(nomPromotion, anneeDebut);

            if (semestres.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(semestres);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }
}