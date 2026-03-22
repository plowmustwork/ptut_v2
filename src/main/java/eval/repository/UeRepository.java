package eval.repository;

import eval.dto.UeDTO;
import eval.entity.Ue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UeRepository extends JpaRepository<Ue, String> {
    Optional<Ue> findByIdUE(String idUE);
    boolean existsByIdUE(String idUE);

    @Query(value = """
        SELECT u.id_ue AS idUE, u.intitule AS intitule
        FROM etudiant e
        JOIN inscription_promotion ip ON ip.etudiant_id = e.id
        JOIN semestre s ON s.promotion_id = ip.promotion_id AND s.annee_id = ip.annee_id
        JOIN ue u ON u.semestre_id = s.id_semestre
        WHERE e.mail = :mail
        AND s.nom_semestre = :nomSemestre
        """, nativeQuery = true)
    List<UeDTO> findUesByEtudiantMailAndSemestre(
        @Param("mail") String mail,
        @Param("nomSemestre") String nomSemestre
    );
}