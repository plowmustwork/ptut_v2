package eval.controller;

import eval.entity.Enseignant;
import eval.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enseignants")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService service;

    @PostMapping
    public ResponseEntity<Enseignant> creer(@RequestBody Enseignant enseignant) {
        return ResponseEntity.ok(service.creer(
            enseignant.getMail(),
            enseignant.getNom(),
            enseignant.getPrenom(),
            enseignant.getMotDePasse()
        ));
    }

    @GetMapping
    public ResponseEntity<List<Enseignant>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enseignant> getById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}