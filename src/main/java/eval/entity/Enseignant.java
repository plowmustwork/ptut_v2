package eval.entity;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.security.MessageDigest;


@Entity
@Getter @Setter @NoArgsConstructor  @RequiredArgsConstructor @ToString

public class Enseignant {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String mail;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @NotNull
    private String motDePasse;


    @ManyToMany
    @JoinTable(
        name = "enseignant_enseignement",
        joinColumns = @JoinColumn(name = "enseignant_mail"),
        inverseJoinColumns = @JoinColumn(name = "enseignement_id")
    )
    private List<Enseignement> enseignements;

    public void setMail(String mail) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(mail.getBytes(StandardCharsets.UTF_8));
            this.mail = HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 indisponible", e);
        }
    }

    /** Hache le mot de passe en Argon2 avant persistance */
    public void setMotDePasse(String motDePasse) {
        var encoder = new org.springframework.security.crypto.argon2.Argon2PasswordEncoder(
            16,   // saltLength
            32,   // hashLength
            1,    // parallelism
            65536,// memory (64 Mo)
            3     // iterations
        );
        this.motDePasse = encoder.encode(motDePasse);
    }
}