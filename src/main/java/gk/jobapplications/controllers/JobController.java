package gk.jobapplications.controllers;

import gk.jobapplications.dtos.CreateJobDTO;
import gk.jobapplications.responses.ApiResponse;
import gk.jobapplications.services.JobService;
import gk.jobapplications.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.entities.CompanyEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobEntity>> createJob(@RequestBody CreateJobDTO createJobDTO) {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(createJobDTO.getCompanyId());

        JobEntity jobEntity = JobEntity.builder()
                .title(createJobDTO.getTitle())
                .description(createJobDTO.getDescription())
                .quantity(createJobDTO.getQuantity())
                .companyEntity(companyEntity)
                .build();

        JobEntity job = jobService.createJob(jobEntity);
        ApiResponse<JobEntity> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Vaga criada com sucesso",
                job
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobEntity>>> getAllJobs() {
        List<JobEntity> jobs = jobService.getAllJobs();
        ApiResponse<List<JobEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vagas retornadas com sucesso",
                jobs
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<JobEntity>>> getJobsByCompany(@PathVariable UUID companyId) {
        List<JobEntity> jobs = jobService.getJobsByCompany(companyId);
        ApiResponse<List<JobEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vagas retornadas com sucesso",
                jobs
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobEntity>> getJobById(@PathVariable UUID id) {
        JobEntity job = jobService.getJobById(id);
        ApiResponse<JobEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vaga retornada com sucesso",
                job
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobEntity>> updateJob(@PathVariable UUID id, @RequestBody CreateJobDTO createJobDTO) {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(createJobDTO.getCompanyId());

        JobEntity jobEntity = JobEntity.builder()
                .title(createJobDTO.getTitle())
                .description(createJobDTO.getDescription())
                .quantity(createJobDTO.getQuantity())
                .companyEntity(companyEntity)
                .build();

        JobEntity job = jobService.updateJob(id, jobEntity);
        ApiResponse<JobEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vaga atualizada com sucesso",
                job
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{jobId}/apply/{candidateId}")
    public ResponseEntity<ApiResponse<JobEntity>> applyToJob(
            @PathVariable UUID jobId,
            @PathVariable UUID candidateId) {
        JobEntity job = jobService.applyToJob(jobId, candidateId);
        ApiResponse<JobEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Candidatura realizada com sucesso",
                job
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
