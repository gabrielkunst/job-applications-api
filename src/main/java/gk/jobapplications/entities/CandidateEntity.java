package gk.jobapplications.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "candidate")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @Email(message = "O campo deve conter um e-mail válido!")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 50, message = "O campo deve contar no máximo 50 caracteres!")
    @Column(nullable = false)
    private String profession;

    @Length(min = 6, max = 10, message = "A senha deve contar entre 6 e 10 caracteres")
    @Column(name = "password", nullable = false)
    private String passwordHash;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "delete_at")
    private LocalDateTime deletedAt;
}
