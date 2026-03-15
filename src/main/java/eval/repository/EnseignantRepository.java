package eval.repository;

import eval.entity.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    // Recherche par mail déjà haché
    Optional<Enseignant> findByMail(String mailHache);

    // Recherche par mail brut (hachage automatique avant requête)
    default Optional<Enseignant> findByMailBrut(String mailBrut) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(mailBrut.getBytes(StandardCharsets.UTF_8));
            String mailHache = HexFormat.of().formatHex(hash);
            return findByMail(mailHache);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 indisponible", e);
        }
    }

    // Recherche par nom
    List<Enseignant> findByNom(String nom);

    // Recherche par nom et prénom
    List<Enseignant> findByNomAndPrenom(String nom, String prenom);

    // Recherche par nom (insensible à la casse)
    List<Enseignant> findByNomIgnoreCase(String nom);

    // Vérifier si un mail (haché) existe déjà
    boolean existsByMail(String mailHache);

    // Tous les enseignants avec leurs enseignements (évite le N+1)
    @Query("SELECT DISTINCT e FROM Enseignant e LEFT JOIN FETCH e.enseignements")
    List<Enseignant> findAllWithEnseignements();

    // Enseignants associés à un enseignement donné
    @Query("SELECT e FROM Enseignant e JOIN e.enseignements ens WHERE ens.id = :enseignementId")
    List<Enseignant> findByEnseignementId(@Param("enseignementId") Long enseignementId);
}