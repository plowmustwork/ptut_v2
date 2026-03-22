package eval.repository;

import eval.dto.UeAvecEnseignementsDTO;
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
    SELECT u.id_ue AS idUE, u.intitule AS intitule,
           e.code AS code, e.libelle AS libelle
    FROM etudiant et
    JOIN inscription_promotion ip ON ip.etudiant_id = et.id
    JOIN semestre s ON s.promotion_id = ip.promotion_id AND s.annee_id = ip.annee_id
    JOIN ue u ON u.semestre_id = s.id_semestre
    LEFT JOIN enseignement e ON e.ue_id = u.id_ue
    WHERE et.mail = :mail
    AND s.nom_semestre = :nomSemestre
    """, nativeQuery = true)
    List<UeAvecEnseignementsDTO> findUesByEtudiantMailAndSemestre(
        @Param("mail") String mail,
        @Param("nomSemestre") String nomSemestre
    );
}