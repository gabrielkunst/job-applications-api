package gk.jobapplications.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "job")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Size(max = 50, message = "O título deve ter no máximo 50 caracteres")
  @Column(nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "company_id", nullable = false)
  private CompanyEntity companyEntity;

  @Column(nullable = false)
  private int quantity;

  @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
  @Column(nullable = false)
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @ManyToMany
  @JoinTable(
          name = "job_candidates",
          joinColumns = @JoinColumn(name = "job_id"),
          inverseJoinColumns = @JoinColumn(name = "candidate_id")
  )
  private List<CandidateEntity> candidates;
}
