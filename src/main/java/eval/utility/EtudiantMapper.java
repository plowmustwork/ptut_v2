package eval.utility;

import eval.dto.EtudiantDTO;
import eval.entity.Etudiant;
import eval.entity.InscriptionPromotion;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class EtudiantMapper {

    public EtudiantDTO toDto(Etudiant etudiant) {
        if (etudiant == null) {
            return null;
        }

        EtudiantDTO dto = new EtudiantDTO();
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setMail(etudiant.getMail());
        dto.setLv2(etudiant.getLv2());

        // Get the first inscription (most recent or only one)
        List<InscriptionPromotion> inscriptions = etudiant.getInscriptions();
        if (inscriptions != null && !inscriptions.isEmpty()) {
            InscriptionPromotion inscription = inscriptions.get(0);

            if (inscription.getPromotion() != null) {
                dto.setPromotionNom(inscription.getPromotion().getNomPromotion());
                dto.setPromotionId(inscription.getPromotion().getId());
            }

            if (inscription.getAnnee() != null) {
                dto.setAnneeId(inscription.getAnnee().getIdAnnee());
                dto.setAnneeDebut(inscription.getAnnee().getAnneeDebut()); // ✅ if you want to expose it
                dto.setAnneeFin(inscription.getAnnee().getAnneeFin());
            }
        }

        return dto;
    }
}