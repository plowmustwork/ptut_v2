package eval.controller;

import eval.entity.Enseignant;
import eval.entity.Enseignement;
import eval.repository.EnseignantRepository;
import eval.repository.EnseignementRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/enseignements")
public class EnseignementController {

    private final EnseignementRepository enseignementRepository;
    private final EnseignantRepository enseignantRepository;

    public EnseignementController(EnseignementRepository enseignementRepository,
                                   EnseignantRepository enseignantRepository) {
        this.enseignementRepository = enseignementRepository;
        this.enseignantRepository = enseignantRepository;
    }

    @PutMapping("/{enseignementId}/enseignants/{enseignantId}")
    public ResponseEntity<Void> addEnseignant(
            @PathVariable Long enseignementId,
            @PathVariable Long enseignantId) {

        Enseignement enseignement = enseignementRepository.findById(enseignementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Enseignement introuvable : " + enseignementId));

        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Enseignant introuvable : " + enseignantId));

        if (!enseignant.getEnseignements().contains(enseignement)) {
            enseignant.getEnseignements().add(enseignement);
            enseignantRepository.save(enseignant);
        }

        return ResponseEntity.noContent().build();
    }
}