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

    @OneToMany(mappedBy = "etudiant")
    private List<InscriptionPromotion> inscriptions;

    @OneToMany(mappedBy = "etudiant")
    private List<Participation> participations;

    @NotNull
    private String lv2;

    public Etudiant() {
    }
}