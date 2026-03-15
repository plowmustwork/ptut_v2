package eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import eval.entity.Promotion;
import java.util.Optional;


public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Optional<Promotion> findById(Long id);
    Optional<Promotion> findByNomPromotion(String nomPromotion);
    
}
