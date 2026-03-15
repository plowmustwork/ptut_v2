package eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import eval.entity.Etudiant;


public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

    Etudiant findById(Long id);
    
}
