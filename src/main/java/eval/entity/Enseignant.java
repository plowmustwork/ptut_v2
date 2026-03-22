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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String mail;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;


    @ManyToMany
    @JoinTable(
        name = "enseignant_enseignement",
        joinColumns = @JoinColumn(name = "enseignant_id"),       // FK → Enseignant.id
        inverseJoinColumns = @JoinColumn(name = "enseignement_id") // FK → Enseignement.id
    )
    private List<Enseignement> enseignements;


}