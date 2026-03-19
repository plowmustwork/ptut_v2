package eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import eval.entity.Ue;

public interface UeRepository extends JpaRepository<Ue, Long> {
    Optional<Ue> findByIdUE(Long idUE);
}
