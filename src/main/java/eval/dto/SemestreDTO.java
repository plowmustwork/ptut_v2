package eval.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemestreDTO {
    private Long idSemestre;
    private String nomSemestre;
    private String anneeDebut;
    private String anneeFin;
    private List<UeDTO> ues;
}