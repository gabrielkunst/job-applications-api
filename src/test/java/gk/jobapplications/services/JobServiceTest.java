package gk.jobapplications.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceInvalidException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CandidateRepository;
import gk.jobapplications.repositories.CompanyRepository;
import gk.jobapplications.repositories.JobRepository;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private JobService jobService;

    private JobEntity jobEntity;
    private CandidateEntity candidateEntity;
    private CompanyEntity companyEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        companyEntity = new CompanyEntity();
        companyEntity.setId(UUID.randomUUID());

        jobEntity = new JobEntity();
        jobEntity.setId(UUID.randomUUID());
        jobEntity.setTitle("Programador");
        jobEntity.setDescription("Desenvolvimento de software");
        jobEntity.setQuantity(5);
        jobEntity.setCompanyEntity(companyEntity);

        candidateEntity = new CandidateEntity();
        candidateEntity.setId(UUID.randomUUID());
    }

    @Test
    void testCreateJob_CompanyExists() {
        when(companyService.getCompanyById(any(UUID.class))).thenReturn(companyEntity);
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobEntity);

        JobEntity result = jobService.createJob(jobEntity);

        assertNotNull(result);
        assertEquals(jobEntity.getTitle(), result.getTitle());
        verify(jobRepository, times(1)).save(jobEntity);
    }

    @Test
    void testCreateJob_CompanyDoesNotExist() {
        when(companyService.getCompanyById(any(UUID.class))).thenReturn(null);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            jobService.createJob(jobEntity);
        });
    }

    @Test
    void testGetJobById_JobExists() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));

        JobEntity result = jobService.getJobById(jobEntity.getId());

        assertNotNull(result);
        assertEquals(jobEntity.getId(), result.getId());
    }

    @Test
    void testGetJobById_JobDoesNotExist() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            jobService.getJobById(jobEntity.getId());
        });
    }

    @Test
    void testUpdateJob_JobExists() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobEntity);

        jobEntity.setTitle("Novo Título");
        JobEntity result = jobService.updateJob(jobEntity.getId(), jobEntity);

        assertNotNull(result);
        assertEquals("Novo Título", result.getTitle());
        verify(jobRepository, times(1)).save(jobEntity);
    }

    @Test
    void testUpdateJob_JobDoesNotExist() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            jobService.updateJob(jobEntity.getId(), jobEntity);
        });
    }

    @Test
    void testDeleteJob_JobExists() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));

        jobService.deleteJob(jobEntity.getId());

        verify(jobRepository, times(1)).save(jobEntity);
        assertNotNull(jobEntity.getDeletedAt());
    }

    @Test
    void testDeleteJob_JobDoesNotExist() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            jobService.deleteJob(jobEntity.getId());
        });
    }

    @Test
    void testGetAllJobs() {
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(jobEntity);

        when(jobRepository.findAll()).thenReturn(jobs);

        List<JobEntity> result = jobService.getAllJobs();

        assertEquals(1, result.size());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void testGetJobsByCompany_CompanyExists() {
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(jobEntity);

        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity));
        when(jobRepository.findAllByCompanyEntity(any(CompanyEntity.class))).thenReturn(jobs);

        List<JobEntity> result = jobService.getJobsByCompany(companyEntity.getId());

        assertEquals(1, result.size());
        verify(jobRepository, times(1)).findAllByCompanyEntity(companyEntity);
    }

    @Test
    void testGetJobsByCompany_CompanyDoesNotExist() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            jobService.getJobsByCompany(companyEntity.getId());
        });
    }

    @Test
    void testApplyToJob_JobAndCandidateExist() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.of(candidateEntity));
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobEntity);

        JobEntity result = jobService.applyToJob(jobEntity.getId(), candidateEntity.getId());

        assertNotNull(result);
        assertTrue(result.getCandidates().contains(candidateEntity));
        verify(jobRepository, times(1)).save(jobEntity);
    }

    @Test
    void testApplyToJob_JobDoesNotExist() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobService.applyToJob(jobEntity.getId(), candidateEntity.getId());
        });
    }

    @Test
    void testApplyToJob_CandidateDoesNotExist() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobService.applyToJob(jobEntity.getId(), candidateEntity.getId());
        });
    }

    @Test
    void testApplyToJob_JobAlreadyApplied() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.of(candidateEntity));

        jobService.applyToJob(jobEntity.getId(), candidateEntity.getId());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            jobService.applyToJob(jobEntity.getId(), candidateEntity.getId());
        });
    }

    @Test
    void testApplyToJob_JobDeleted() {
        when(jobRepository.findById(any(UUID.class))).thenReturn(Optional.of(jobEntity));
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.of(candidateEntity));

        jobService.deleteJob(jobEntity.getId());

        assertThrows(ResourceInvalidException.class, () -> {
            jobService.applyToJob(jobEntity.getId(), candidateEntity.getId());
        });
    }

    @Test
    void testGetAllActiveJobs() {
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(jobEntity);

        when(jobRepository.findByDeletedAtIsNull()).thenReturn(jobs);

        List<JobEntity> result = jobService.getAllActiveJob();

        assertEquals(1, result.size());
        verify(jobRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    void testGetAllInactiveJobs() {
        jobEntity.setDeletedAt(LocalDateTime.now());
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(jobEntity);

        when(jobRepository.findByDeletedAtIsNotNull()).thenReturn(jobs);

        List<JobEntity> result = jobService.getAllInactiveJob();

        assertEquals(1, result.size());
        verify(jobRepository, times(1)).findByDeletedAtIsNotNull();
    }
}
