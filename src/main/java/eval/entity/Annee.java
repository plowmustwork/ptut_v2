package eval.entity;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity @Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Annee {
    @Id
    @Column(name = "annee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnnee;

    @NotNull
    private Integer dateDebut;

    @NotNull
    private Integer dateFin;

    @ManyToMany
    @JoinTable(
        name = "annee_promotion",
        joinColumns = @JoinColumn(name = "annee_id"),
        inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<Promotion> promotions;
}
