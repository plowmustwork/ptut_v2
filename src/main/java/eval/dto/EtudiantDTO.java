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
    private Long anneeId; 
    private String anneeDebut; // adjust type to match your Annee entity (LocalDate, String, etc.)
    private String anneeFin;


    public void EtudiantMapper(Etudiant etudiant){
        this.nom = etudiant.getNom();
        this.prenom = etudiant.getPrenom();
        this.mail = etudiant.getMail();
        this.promotionNom = etudiant.getInscriptions().get(etudiant.getInscriptions().size() - 1).getPromotion().getNomPromotion(); // Assuming the last inscription is the relevant one
        this.lv2 = etudiant.getLv2();
        this.promotionId = etudiant.getInscriptions().get(etudiant.getInscriptions().size() - 1).getPromotion().getId(); // Assuming the last inscription is the relevant one
        this.anneeId = etudiant.getInscriptions().get(etudiant.getInscriptions().size() - 1).getAnnee().getIdAnnee(); // Assuming the last inscription is the relevant one
        this.anneeDebut = etudiant.getInscriptions().get(etudiant.getInscriptions().size() - 1).getAnnee().getAnneeDebut().toString(); // Assuming the last inscription is the relevant one
        this.anneeFin = etudiant.getInscriptions().get(etudiant.getInscriptions().size() - 1).getAnnee().getAnneeFin().toString(); // Assuming the last inscription is the relevant one
    }


}
