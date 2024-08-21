package gk.jobapplications.services;

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

    public JobEntity createJob(JobEntity jobEntity) {
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

    public JobEntity getJobByTitle(String title) {
        return jobRepository.findByTitle(title);
    }
}
