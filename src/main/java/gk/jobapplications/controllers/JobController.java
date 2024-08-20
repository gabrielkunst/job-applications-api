package gk.jobapplications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.services.JobService;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
  
  @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<JobEntity> createJob(@RequestBody JobEntity jobEntity) {
        JobEntity Job = jobService.createJob(jobEntity);
        return new ResponseEntity<JobEntity>(Job, HttpStatus.CREATED);
    }

}
