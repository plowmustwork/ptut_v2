package eval.service;

import eval.dto.EnseignementDTO;
import eval.dto.UeDTO;
import eval.repository.EnseignementRepository;
import eval.repository.UeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UeService {

    @Autowired
    private UeRepository ueRepository;

    @Autowired
    private EnseignementRepository enseignementRepository;

    public List<UeDTO> getUesByEtudiantMailAndSemestre(String mail, String nomSemestre) {
        return ueRepository.findUesByEtudiantMailAndSemestre(mail, nomSemestre);
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