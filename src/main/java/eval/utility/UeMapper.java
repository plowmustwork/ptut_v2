package eval.utility;

import eval.dto.EnseignementDTO;
import eval.dto.UeDTO;
import eval.entity.Enseignement;
import eval.entity.Ue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UeMapper {

    public UeDTO toDto(Ue ue) {
        if (ue == null) return null;

        UeDTO dto = new UeDTO();
        dto.setIdUE(ue.getIdUE());
        dto.setIntitule(ue.getIntitule());

        List<EnseignementDTO> ensDtos = new ArrayList<>();
        for (Enseignement e : ue.getEnseignements()) {
            EnseignementDTO edto = new EnseignementDTO();
            edto.setId(e.getId());
            edto.setCode(e.getCode());
            edto.setLibelle(e.getLibelle());
            ensDtos.add(edto);
        }
        dto.setEnseignements(ensDtos);

        return dto;
    }

    public Ue toEntity(UeDTO dto) {
        if (dto == null) return null;

        Ue ue = new Ue();
        ue.setIdUE(dto.getIdUE());
        ue.setIntitule(dto.getIntitule());

        if (dto.getEnseignements() != null) {
            for (EnseignementDTO edto : dto.getEnseignements()) {
                Enseignement e = new Enseignement();
                e.setCode(edto.getCode());
                e.setLibelle(edto.getLibelle());
                ue.addEnseignement(e);
            }
        }

        return ue;
    }
}