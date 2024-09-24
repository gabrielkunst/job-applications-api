package gk.jobapplications.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.entities.JobEntity;


@Repository
public interface JobRepository extends JpaRepository<JobEntity, UUID> {
  List<JobEntity> findAllByCompanyEntity(CompanyEntity companyEntity);
  List<JobEntity> findByDeletedAtIsNull();
  List<JobEntity> findByDeletedAtIsNotNull();


}
