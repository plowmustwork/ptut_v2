package eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eval.entity.InscriptionPromotion;

@Repository
public interface InscriptionPromotionRepository extends JpaRepository<InscriptionPromotion, Long> {
    boolean existsByEtudiantIdAndPromotionId(Long etudiantId, Long promotionId);
}
