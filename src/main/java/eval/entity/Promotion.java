package eval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.List;
@Entity
@Getter @Setter
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long id;

    @NotNull
    private String nomPromotion;

    @OneToMany(mappedBy = "promotion")
    private List<InscriptionPromotion> inscriptions;

    @OneToMany(mappedBy = "promotion")
    private List<Semestre> semestres;

    @ManyToMany(mappedBy = "promotions")
    private List<Annee> annees;
}