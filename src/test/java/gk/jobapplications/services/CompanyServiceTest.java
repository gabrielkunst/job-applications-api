package gk.jobapplications.services;

import gk.jobapplications.dtos.JobKPIDTO;
import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CompanyRepository;
import gk.jobapplications.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class CompanyServiceTest {

    private CompanyService companyService;
    private CompanyRepository companyRepository;
    private JobRepository jobRepository;

    @BeforeEach
    public void setUp() {
        companyRepository = Mockito.mock(CompanyRepository.class);
        jobRepository = Mockito.mock(JobRepository.class);
        companyService = new CompanyService(companyRepository, jobRepository);
    }

    @Test
    public void testCreateCompanySuccess() {
        CompanyEntity newCompany = new CompanyEntity();
        newCompany.setCnpj("123456789");

        Mockito.when(companyRepository.findByCnpj(anyString())).thenReturn(null);
        Mockito.when(companyRepository.save(any(CompanyEntity.class))).thenReturn(newCompany);

        CompanyEntity createdCompany = companyService.createCompany(newCompany);
        assertEquals(newCompany.getCnpj(), createdCompany.getCnpj());
    }

    @Test
    public void testCreateCompanyAlreadyExists() {
        CompanyEntity existingCompany = new CompanyEntity();
        existingCompany.setCnpj("123456789");

        Mockito.when(companyRepository.findByCnpj(anyString())).thenReturn(existingCompany);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            companyService.createCompany(existingCompany);
        });
    }

    @Test
    public void testGetCompanyByIdSuccess() {
        UUID id = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(id);

        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        CompanyEntity foundCompany = companyService.getCompanyById(id);
        assertEquals(company.getId(), foundCompany.getId());
    }

    @Test
    public void testGetCompanyByIdNotFound() {
        UUID id = UUID.randomUUID();

        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.getCompanyById(id);
        });
    }

    @Test
    public void testUpdateCompanySuccess() {
        UUID id = UUID.randomUUID();
        CompanyEntity existingCompany = new CompanyEntity();
        existingCompany.setId(id);
        existingCompany.setCnpj("123456789");

        CompanyEntity updatedCompany = new CompanyEntity();
        updatedCompany.setName("Updated Company");

        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.of(existingCompany));
        Mockito.when(companyRepository.save(any(CompanyEntity.class))).thenReturn(existingCompany);

        CompanyEntity result = companyService.updateCompany(id, updatedCompany);
        assertEquals("Updated Company", result.getName());
    }

    @Test
    public void testUpdateCompanyNotFound() {
        UUID id = UUID.randomUUID();
        CompanyEntity updatedCompany = new CompanyEntity();

        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.updateCompany(id, updatedCompany);
        });
    }

    @Test
    public void testDeleteCompanySuccess() {
        UUID id = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(id);

        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.of(company));
        Mockito.when(companyRepository.save(any(CompanyEntity.class))).thenReturn(company);

        companyService.deleteCompany(id);
        assertNotNull(company.getDeletedAt());
    }

    @Test
    public void testDeleteCompanyNotFound() {
        UUID id = UUID.randomUUID();

        Mockito.when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.deleteCompany(id);
        });
    }

    @Test
    public void testGetAllActiveCompanies() {
        List<CompanyEntity> companies = new ArrayList<>();
        companies.add(new CompanyEntity());
        Mockito.when(companyRepository.findByDeletedAtIsNull()).thenReturn(companies);

        List<CompanyEntity> result = companyService.getAllActiveCompanies();
        assertEquals(companies.size(), result.size());
    }

    @Test
    public void testGetAllInactiveCompanies() {
        List<CompanyEntity> companies = new ArrayList<>();
        companies.add(new CompanyEntity());
        Mockito.when(companyRepository.findByDeletedAtIsNotNull()).thenReturn(companies);

        List<CompanyEntity> result = companyService.getAllInactiveCompanies();
        assertEquals(companies.size(), result.size());
    }

    @Test
    public void testGetCompanyKPISuccess() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);

        List<JobEntity> jobs = new ArrayList<>();
        JobEntity job1 = new JobEntity();
        job1.setCandidates(new ArrayList<>());
        jobs.add(job1);

        JobEntity job2 = new JobEntity();
        job2.setCandidates(new ArrayList<>());
        jobs.add(job2);

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        Mockito.when(jobRepository.findAllByCompanyEntity(company)).thenReturn(jobs);

        JobKPIDTO kpi = companyService.getCompanyKPIS(companyId);
        assertEquals(2, kpi.getTotalJobs());
        assertEquals(0, kpi.getTotalCandidates());
    }

    @Test
    public void testGetCompanyKPINotFound() {
        UUID companyId = UUID.randomUUID();

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.getCompanyKPIS(companyId);
        });
    }
}
