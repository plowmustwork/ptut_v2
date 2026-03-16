package eval.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import eval.entity.Promotion;

@Data @AllArgsConstructor @NoArgsConstructor

public class PromotionDTO {
    private String nomPromotion;
    public void PromotionMapper(Promotion promotion){
        this.nomPromotion = promotion.getNomPromotion();
    }
}

