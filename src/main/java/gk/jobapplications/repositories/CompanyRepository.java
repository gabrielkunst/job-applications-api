package gk.jobapplications.repositories;

import gk.jobapplications.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
    CompanyEntity findByCnpj(String cnpj);
}
