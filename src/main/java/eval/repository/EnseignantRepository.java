package eval.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import eval.entity.Enseignant;
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {}