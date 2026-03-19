package eval.entity;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;


@Entity@Getter @Setter @ToString

public class Ue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ue_id")
    private Long idUE;

    @NotNull
    private String libelle;

    @OneToMany(mappedBy = "ue")
    private List<Enseignement> listeCours;

    @ManyToOne
    @JoinColumn(name = "semestre_id")
    private Semestre semestre;

}
