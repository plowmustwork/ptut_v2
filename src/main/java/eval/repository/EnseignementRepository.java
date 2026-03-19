package eval.repository;

import eval.entity.Enseignement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {
}