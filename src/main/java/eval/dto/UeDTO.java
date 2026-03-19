package eval.dto;

import java.util.ArrayList;
import java.util.List;

public class UeDTO {
    private String idUE;
    private String intitule;
    private List<EnseignementDTO> enseignements = new ArrayList<>();

    public String getIdUE() {
        return idUE;
    }

    public void setIdUE(String idUE) {
        this.idUE = idUE;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<EnseignementDTO> getEnseignements() {
        return enseignements;
    }

    public void setEnseignements(List<EnseignementDTO> enseignements) {
        this.enseignements = enseignements;
    }
}