package eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import eval.entity.Etudiant;
import java.util.List;
import java.util.Optional;


public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

    Etudiant findById(Long id);

    @RestResource(path = "findByPrenom")                          // ✅ Nouveau
    List<Etudiant> findByPrenomContainingIgnoreCase(@Param("prenom") String prenom);

    Optional<Etudiant> findByMailIgnoreCase(String mail);
    
}
