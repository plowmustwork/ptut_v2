package eval.service;

import eval.dto.EnseignementDTO;
import eval.dto.UeAvecEnseignementsDTO;
import eval.dto.UeDTO;
import eval.repository.EnseignementRepository;
import eval.repository.UeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UeService {

    @Autowired
    private UeRepository ueRepository;

    @Autowired
    private EnseignementRepository enseignementRepository;

    public List<UeDTO> getUesByEtudiantMailAndSemestre(String mail, String nomSemestre) {
        List<UeAvecEnseignementsDTO> rows = ueRepository.findUesByEtudiantMailAndSemestre(mail, nomSemestre);

        // Group enseignements by UE
        Map<String, UeDTO> ueMap = new LinkedHashMap<>();
        for (UeAvecEnseignementsDTO row : rows) {
            ueMap.computeIfAbsent(row.getIdUE(), id -> {
                UeDTO dto = new UeDTO();
                dto.setIdUE(row.getIdUE());
                dto.setIntitule(row.getIntitule());
                dto.setEnseignements(new ArrayList<>());
                return dto;
            });

            // Add enseignement if it exists (LEFT JOIN can return null)
            if (row.getCode() != null) {
                EnseignementDTO ens = new EnseignementDTO();
                ens.setCode(row.getCode());
                ens.setLibelle(row.getLibelle());
                ueMap.get(row.getIdUE()).getEnseignements().add(ens);
            }
        }

        return new ArrayList<>(ueMap.values());
    }

    public List<EnseignementDTO> getEnseignementsByUe(String idUe) {
        if (!ueRepository.existsByIdUE(idUe)) {
            throw new RuntimeException("UE introuvable: " + idUe);
        }
        return enseignementRepository.findByUe_IdUE(idUe)
            .stream()
            .map(e -> new EnseignementDTO(e.getId(), e.getCode(), e.getLibelle()))
            .collect(Collectors.toList());
    }
}