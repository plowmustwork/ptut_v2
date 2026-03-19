package eval.controller;

import eval.dto.UeDTO;
import eval.service.UeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ue")
public class UeController {

    private final UeService ueService;

    public UeController(UeService ueService) {
        this.ueService = ueService;
    }

    @PostMapping
    public ResponseEntity<UeDTO> creer(@RequestBody UeDTO dto) {
        return ResponseEntity.ok(ueService.creer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UeDTO> trouver(@PathVariable String id) {
        UeDTO ue = ueService.trouver(id);
        return ue == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(ue);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importerExcel(@RequestParam("file") MultipartFile file) {
        try {
            int nb = ueService.importerExcel(file);
            return ResponseEntity.ok(Map.of("message", "Import terminé", "ueImportees", nb));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Lecture du fichier impossible"));
        }
    }
}