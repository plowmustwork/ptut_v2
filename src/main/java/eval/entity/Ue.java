package eval.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ue")
public class Ue {

    @Id
    @Column(name = "id_ue", nullable = false, length = 50)
    private String idUE;

    @Column(nullable = false, length = 500)
    private String intitule;

    @OneToMany(mappedBy = "ue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enseignement> enseignements = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semestre_id")
    private Semestre semestre;

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

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

    public List<Enseignement> getEnseignements() {
        return enseignements;
    }

    public void setEnseignements(List<Enseignement> enseignements) {
        this.enseignements.clear();
        if (enseignements != null) {
            enseignements.forEach(this::addEnseignement);
        }
    }

    public void addEnseignement(Enseignement enseignement) {
        enseignements.add(enseignement);
        enseignement.setUe(this);
    }

    public void removeEnseignement(Enseignement enseignement) {
        enseignements.remove(enseignement);
        enseignement.setUe(null);
    }
}