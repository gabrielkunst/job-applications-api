package gk.jobapplications.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceInvalidException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CandidateRepository;
import gk.jobapplications.repositories.CompanyRepository;
import gk.jobapplications.repositories.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    public JobEntity createJob(JobEntity jobEntity) {
        CompanyEntity companyFromDB = companyService.getCompanyById(jobEntity.getCompanyEntity().getId());

        if (companyFromDB == null) {
            throw new ResourceAlreadyExistsException("Empresa não encontrada");
        }

        jobEntity.setCompanyEntity(companyFromDB);
        return jobRepository.save(jobEntity);
    }

    public JobEntity getJobById(UUID id) {
        JobEntity jobFromDB = jobRepository.findById(id).orElse(null);

        if (jobFromDB == null) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        return jobFromDB;
    }

    public JobEntity updateJob(UUID id, JobEntity jobEntity) {
        JobEntity jobFromDB = jobRepository.findById(id).orElse(null);

        if (jobFromDB == null) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        jobFromDB.setTitle(jobEntity.getTitle());
        jobFromDB.setDescription(jobEntity.getDescription());
        jobFromDB.setQuantity(jobEntity.getQuantity());

        return jobRepository.save(jobFromDB);
    }

    public void deleteJob(UUID id) {
        JobEntity jobFromDB = jobRepository.findById(id).orElse(null);

        if (jobFromDB == null) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        jobFromDB.setDeletedAt(LocalDateTime.now());
        jobRepository.save(jobFromDB);
    }

    public List<JobEntity> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<JobEntity> getJobsByCompany(UUID id) {
        CompanyEntity companyFromDB = companyRepository.findById(id).orElse(null);

        if (companyFromDB == null) {
            throw new ResourceAlreadyExistsException("Empresa não encontrada");
        }

        return jobRepository.findAllByCompanyEntity(companyFromDB);
    }

    public JobEntity getJobByTitle(String title) {
        JobEntity jobFromDB = jobRepository.findByTitle(title).orElse(null);

        if (jobFromDB == null) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        return jobFromDB;
    }

    public JobEntity applyToJob(UUID jobId, UUID candidateId) {
        JobEntity jobFromDB = jobRepository.findById(jobId).orElse(null);
        CandidateEntity candidateFromDB = candidateRepository.findById(candidateId).orElse(null);

        if (jobFromDB == null) {
            throw new ResourceNotFoundException("Vaga não encontrada");
        }

        if (jobFromDB.getDeletedAt() != null) {
            throw new ResourceInvalidException("Vaga não está mais disponível");
        }

        if (candidateFromDB == null) {
            throw new ResourceNotFoundException("Candidato não encontrado");
        }

        if (jobFromDB.getCandidates().contains(candidateFromDB)) {
            throw new ResourceAlreadyExistsException("Candidato já se aplicou a esta vaga");
        }

        jobFromDB.getCandidates().add(candidateFromDB);
        return jobRepository.save(jobFromDB);
    }

    public List<JobEntity> getAllActiveJob() {
        return jobRepository.findByDeletedAtIsNull();
    }

    public List<JobEntity> getAllInactiveJob() {
        return jobRepository.findByDeletedAtIsNotNull();
    }
}
