package eval.entity;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;



@Entity
@Getter @Setter @ToString

public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String mail;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @ManyToOne
    @JoinColumn(name = "promotion_nom_promotion")  
    private Promotion promotion;

    @OneToMany(mappedBy = "etudiant")
    private List<Participation> participations;

    @NotNull
    private String lv2;

    /*
    @ManyToMany
    @JoinTable(
        name = "etudiant_enseignement",
        joinColumns = @JoinColumn(name = "etudiant_mail"),
        inverseJoinColumns = @JoinColumn(name = "enseignement_id")
    )
    private List<Enseignement> enseignements;
    */

    public Etudiant() {
    }

}
