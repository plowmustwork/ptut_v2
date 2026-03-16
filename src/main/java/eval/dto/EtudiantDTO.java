package eval.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import eval.entity.Etudiant;

@Data @AllArgsConstructor @NoArgsConstructor
public class EtudiantDTO {
    private String nom;
    private String prenom;
    private String mail;
    private String promotionNom;
    private String lv2;
    private Long promotionId;


    public void EtudiantMapper(Etudiant etudiant){
        this.nom = etudiant.getNom();
        this.prenom = etudiant.getPrenom();
        this.mail = etudiant.getMail();
        this.promotionNom = etudiant.getPromotion().getNomPromotion();
        this.lv2 = etudiant.getLv2();
        this.promotionId = etudiant.getPromotion().getId();

    }


}
