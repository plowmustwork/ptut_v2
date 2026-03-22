package eval.service;

import eval.dto.EnseignementDTO;
import eval.dto.SemestreDTO;
import eval.dto.UeDTO;
import eval.entity.Semestre;
import eval.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemestreService {

    @Autowired
    private SemestreRepository semestreRepository;

    public List<SemestreDTO> getSemestresByPromotionAndAnnee(String nomPromotion, String anneeDebut) {
        List<Semestre> semestres = semestreRepository
            .findByPromotionNomPromotionAndAnneeAnneeDebut(nomPromotion, anneeDebut);

        return semestres.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private SemestreDTO toDto(Semestre semestre) {
        SemestreDTO dto = new SemestreDTO();
        dto.setIdSemestre(semestre.getIdSemestre());
        dto.setNomSemestre(semestre.getNomSemestre());

        if (semestre.getAnnee() != null) {
            dto.setAnneeDebut(semestre.getAnnee().getAnneeDebut());
            dto.setAnneeFin(semestre.getAnnee().getAnneeFin());
        }

        // Map UEs with their enseignements
        if (semestre.getUes() != null) {
            List<UeDTO> ueDtos = semestre.getUes().stream().map(ue -> {
                UeDTO ueDto = new UeDTO();
                ueDto.setIdUE(ue.getIdUE());
                ueDto.setIntitule(ue.getIntitule());

                List<EnseignementDTO> ensDtos = ue.getEnseignements() == null
                    ? new ArrayList<>()
                    : ue.getEnseignements().stream().map(e -> {
                        EnseignementDTO ensDto = new EnseignementDTO();
                        ensDto.setId(e.getId());
                        ensDto.setCode(e.getCode());
                        ensDto.setLibelle(e.getLibelle());
                        return ensDto;
                    }).collect(Collectors.toList());

                ueDto.setEnseignements(ensDtos);
                return ueDto;
            }).collect(Collectors.toList());

            dto.setUes(ueDtos);
        }

        return dto;
    }
}