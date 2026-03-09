package eval.repository;

import org.apache.poi.sl.draw.geom.GuideIf.Op;
import org.springframework.data.jpa.repository.JpaRepository;

import eval.entity.Etudiant;

import eval.entity.Promotion;
import java.util.Optional;
import java.util.List;


public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Optional<Promotion> findById(Long id);
    Optional<Promotion> findByNomPromotion(String nomPromotion);
    
}
