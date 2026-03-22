package eval.repository;

import eval.entity.Annee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnneeRepository extends JpaRepository<Annee, Long> {
    Optional<Annee> findByAnneeDebut(String anneeDebut);
}