package gk.jobapplications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.repositories.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public JobEntity createJob(JobEntity jobEntity) {
        JobEntity jobFromDB = jobRepository.findByTitle(jobEntity.getTitle());

        if (jobFromDB != null) {
            throw new ResourceAlreadyExistsException("Vaga já cadastrada");
        }

        return jobRepository.save(jobEntity);
    }
}
