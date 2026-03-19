package eval.service;

import eval.dto.UeDTO;
import eval.entity.Enseignement;
import eval.entity.Ue;
import eval.repository.UeRepository;
import eval.utility.UeMapper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UeService {

    private final UeRepository ueRepository;
    private final UeMapper ueMapper;

    public UeService(UeRepository ueRepository, UeMapper ueMapper) {
        this.ueRepository = ueRepository;
        this.ueMapper = ueMapper;
    }

    @Transactional
    public UeDTO creer(UeDTO dto) {
        if (dto == null || dto.getIdUE() == null || dto.getIdUE().isBlank()) {
            throw new IllegalArgumentException("idUE obligatoire");
        }
        if (ueRepository.existsByIdUE(dto.getIdUE())) {
            throw new IllegalArgumentException("UE déjà existante: " + dto.getIdUE());
        }

        Ue saved = ueRepository.save(ueMapper.toEntity(dto));
        return ueMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public UeDTO trouver(String id) {
        return ueRepository.findByIdUE(id).map(ueMapper::toDto).orElse(null);
    }

    @Transactional
    public int importerExcel(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide");
        }

        DataFormatter formatter = new DataFormatter();
        Map<String, Ue> ueMap = new LinkedHashMap<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // ligne 0 = header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String idUE = formatter.formatCellValue(row.getCell(0)).trim();
                String intitule = formatter.formatCellValue(row.getCell(1)).trim();
                String enseignementsRaw = formatter.formatCellValue(row.getCell(2)).trim();

                if (idUE.isBlank()) continue;

                Ue ue = ueMap.computeIfAbsent(idUE, key ->
                        ueRepository.findByIdUE(key).orElseGet(() -> {
                            Ue n = new Ue();
                            n.setIdUE(key);
                            return n;
                        })
                );

                if (!intitule.isBlank()) {
                    ue.setIntitule(intitule);
                }

                List<Enseignement> parsed = parseEnseignements(enseignementsRaw, idUE);
                for (Enseignement e : parsed) {
                    if (!hasEnseignementCode(ue, e.getCode())) {
                        ue.addEnseignement(e);
                    }
                }
            }
        }

        ueRepository.saveAll(ueMap.values());
        return ueMap.size();
    }

    private boolean hasEnseignementCode(Ue ue, String code) {
        return ue.getEnseignements().stream()
                .anyMatch(e -> e.getCode() != null && e.getCode().equalsIgnoreCase(code));
    }

    private List<Enseignement> parseEnseignements(String raw, String idUE) {
        List<Enseignement> list = new ArrayList<>();
        if (raw == null || raw.isBlank()) return list;

        String[] items = raw.split("[;\\n\\r]+");
        int autoIndex = 1;

        for (String item : items) {
            String token = item == null ? "" : item.trim();
            if (token.isBlank()) continue;

            String code;
            String libelle;

            if (token.contains("|")) {
                String[] parts = token.split("\\|", 2);
                code = parts[0].trim();
                libelle = parts[1].trim();
            } else if (token.contains(":")) {
                String[] parts = token.split(":", 2);
                code = parts[0].trim();
                libelle = parts[1].trim();
            } else {
                code = idUE + "-ENS-" + autoIndex++;
                libelle = token;
            }

            if (libelle.isBlank()) continue;
            if (code.isBlank()) code = idUE + "-ENS-" + autoIndex++;

            Enseignement e = new Enseignement();
            e.setCode(truncate(code, 50));
            e.setLibelle(truncate(libelle, 255));
            list.add(e);
        }

        return list;
    }

    private String truncate(String value, int max) {
        if (value == null) return null;
        return value.length() <= max ? value : value.substring(0, max);
    }
}