package gk.jobapplications.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gk.jobapplications.entities.CandidateEntity;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID>{
  CandidateEntity findByEmail(String email);

  List<CandidateEntity> findByDeletedAtIsNull();

  List<CandidateEntity> findByDeletedAtIsNotNull();
}
