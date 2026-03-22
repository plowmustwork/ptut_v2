package eval.entity;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter @Setter
public class InscriptionPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "annee_id")
    private Annee annee;
}