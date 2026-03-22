package eval.utility;

import eval.dto.EtudiantDTO;
import eval.entity.Etudiant;
import org.springframework.stereotype.Component;

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

        if (etudiant.getPromotion() != null) {
            dto.setPromotionNom(etudiant.getPromotion().getNomPromotion());
            dto.setPromotionId(etudiant.getPromotion().getId());
        }

        return dto;
    }
}
