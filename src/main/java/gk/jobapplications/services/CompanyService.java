package gk.jobapplications.services;

import java.util.List;

import gk.jobapplications.dtos.JobKPIDTO;
import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CompanyRepository;


import gk.jobapplications.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobRepository jobRepository;

    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        CompanyEntity companyFromDB = companyRepository.findByCnpj(companyEntity.getCnpj());

        if (companyFromDB != null) {
            throw new ResourceAlreadyExistsException("CNPJ já cadastrado");
        }

        return companyRepository.save(companyEntity);
    }

    public CompanyEntity getCompanyById(UUID id) {
        CompanyEntity companyEntity = companyRepository.findById(id).orElse(null);

        if (companyEntity == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        return companyEntity;
    }

    public CompanyEntity getCompanyByCNPJ(String cnpj) {
        CompanyEntity companyEntity = companyRepository.findByCnpj(cnpj);

        if (companyEntity == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        return companyEntity;
    }

    public CompanyEntity updateCompany(UUID id, CompanyEntity companyEntity) {
        CompanyEntity companyFromDB = companyRepository.findById(id).orElse(null);

        if (companyFromDB == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        companyFromDB.setName(companyEntity.getName());
        companyFromDB.setAddress(companyEntity.getAddress());
        companyFromDB.setPhone(companyEntity.getPhone());

        return companyRepository.save(companyFromDB);
    }

    public void deleteCompany(UUID id) {
        CompanyEntity companyEntity = companyRepository.findById(id).orElse(null);

        if (companyEntity == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        companyEntity.setDeletedAt(LocalDateTime.now());
        companyRepository.save(companyEntity);
    }

    public List<CompanyEntity> getAllActiveCompanies() {
        return companyRepository.findByDeletedAtIsNull();
    }

    public List<CompanyEntity> getAllInactiveCompanies() {
        return companyRepository.findByDeletedAtIsNotNull();
    }


    public JobKPIDTO getCompanyKPIS(UUID companyId) {
        Optional<CompanyEntity> companyFromDB = companyRepository.findById(companyId);

        if (companyFromDB.isEmpty()) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        List<JobEntity> jobs = jobRepository.findAllByCompanyEntity(companyFromDB.get());

        int totalJobs = jobs.size();

        List<JobEntity> activeJobs = jobs.stream().filter(job -> job.getDeletedAt() == null).toList();

        int totalActiveJobs = activeJobs.size();

        List<JobEntity> inactiveJobs = jobs.stream().filter(job -> job.getDeletedAt() != null).toList();

        int totalInactiveJobs = inactiveJobs.size();

        JobEntity mostAppliedJob = jobs.stream().max((job1, job2) -> job1.getCandidates().size() - job2.getCandidates().size()).orElse(null);

        if (mostAppliedJob != null && mostAppliedJob.getCandidates().isEmpty()) {
            mostAppliedJob = null;
        }

        JobKPIDTO jobKPIDTO = JobKPIDTO.builder()
                .totalJobs(totalJobs)
                .activeJobs(totalActiveJobs)
                .inactiveJobs(totalInactiveJobs)
                .totalCandidates(jobs.stream().mapToInt(job -> job.getCandidates().size()).sum())
                .mostAppliedJob(mostAppliedJob)
                .build();

        return jobKPIDTO;
    }
}
