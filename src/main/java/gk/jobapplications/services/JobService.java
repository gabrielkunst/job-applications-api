package gk.jobapplications.services;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.repositories.JobRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

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
}
