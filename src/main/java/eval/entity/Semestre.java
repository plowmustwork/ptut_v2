package eval.entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.List;

@Entity
public class Semestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSemestre;

    @ManyToOne
    @JoinColumn(name = "promotion_id")  
    private Promotion promotion;

    @OneToMany(mappedBy = "semestre")
    private List<Ue> ues;

}
