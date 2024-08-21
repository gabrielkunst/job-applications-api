package gk.jobapplications.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

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

  @ManyToOne()
  @JoinColumn(name = "company_id")
  private CompanyEntity companyEntity;

  @Column(name = "company_id", nullable = false, insertable=false, updatable=false)
  private UUID companyId;

  @Column(nullable = false)
  private int quantity;

  @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
  @Column(nullable = false)
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "delete_at")
  private LocalDateTime deletedAt;
}
