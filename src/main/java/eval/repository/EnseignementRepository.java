package eval.repository;

import eval.dto.EnseignementDTO;
import eval.entity.Enseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {

    @Query(value = """
        SELECT e.id, e.code, e.libelle
        FROM enseignement e
        JOIN ue u ON e.ue_id = u.id_ue
        WHERE u.id_ue = :idUe
        """, nativeQuery = true)
    List<Enseignement> findByUe_IdUE(String idUe);
}