package gk.jobapplications.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gk.jobapplications.entities.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, UUID>{
  JobEntity findById(String Id);
}
