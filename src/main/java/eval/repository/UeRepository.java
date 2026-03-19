package eval.repository;

import eval.entity.Ue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UeRepository extends JpaRepository<Ue, String> {
    Optional<Ue> findByIdUE(String idUE);
    boolean existsByIdUE(String idUE);
}