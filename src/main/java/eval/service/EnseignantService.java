package eval.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import eval.repository.PromotionRepository;
import java.util.Optional;
import java.util.List;
import lombok.*;
import eval.entity.Enseignant;
import eval.repository.EnseignantRepository;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
@Service
@RequiredArgsConstructor
public class EnseignantService {
    @Autowired
    private final EnseignantRepository repository;
    private final Argon2PasswordEncoder argon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    
    

    public Enseignant creer(String mail, String nom, String prenom, String motDePasse) {
        Enseignant e = new Enseignant(mail); // setMail() hache automatiquement
        e.setNom(nom);
        e.setPrenom(prenom);
        e.setMotDePasse(motDePasse);         // setMotDePasse() hache automatiquement
        return repository.save(e);
    }

    /** Vérification du mot de passe lors de la connexion */
    public boolean verifierMotDePasse(String motDePasseSaisi, String hashStocke) {
        return argon2.matches(motDePasseSaisi, hashStocke);
    }


    public List<Enseignant> findAll() {
        return repository.findAll();
    }

    public Optional<Enseignant> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}