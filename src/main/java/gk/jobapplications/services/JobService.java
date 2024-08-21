package gk.jobapplications.services;

import gk.jobapplications.entities.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.repositories.JobRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

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

        System.out.println(jobFromDB);

        if (jobFromDB == null) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        return jobFromDB;
    }

    public JobEntity updateJob(UUID id, JobEntity jobEntity) {
        Optional<JobEntity> optionalJob = jobRepository.findById(id);

        if (optionalJob.isEmpty()) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        JobEntity jobFromDB = optionalJob.get();
        jobFromDB.setTitle(jobEntity.getTitle());
        jobFromDB.setDescription(jobEntity.getDescription());

        return jobRepository.save(jobFromDB);
    }

    public void deleteJob(UUID id) {
        Optional<JobEntity> optionalJob = jobRepository.findById(id);

        if (optionalJob.isEmpty()) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        JobEntity jobFromDB = optionalJob.get();
        jobFromDB.setDeletedAt(LocalDateTime.now());

        jobRepository.save(jobFromDB);
    }

    public List<JobEntity> getAllJobs() {
        return jobRepository.findAll();
    }

    public JobEntity getJobByTitle(String title) {
        Optional<JobEntity> optionalJob = jobRepository.findByTitle(title);

        if (optionalJob.isEmpty()) {
            throw new ResourceAlreadyExistsException("Vaga não encontrada");
        }

        return optionalJob.get();
    }
}
