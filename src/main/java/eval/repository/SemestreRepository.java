package eval.repository;
import eval.entity.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    
}
