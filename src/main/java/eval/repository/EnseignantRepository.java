package eval.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eval.entity.Enseignant;
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    Optional<Enseignant> findByMail(String mail);
}