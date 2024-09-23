package gk.jobapplications.services;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CompanyRepository;
import gk.jobapplications.repositories.JobRepository;
import gk.jobapplications.dtos.JobKPIDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private CompanyService companyService;

    private CompanyEntity companyEntity;
    private UUID companyId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        companyId = UUID.randomUUID();
        companyEntity = new CompanyEntity();
        companyEntity.setId(companyId);
        companyEntity.setName("Empresa Teste");
        companyEntity.setCnpj("123456789");
    }

    @Test
    void testCreateCompany_Success() {
        when(companyRepository.findByCnpj(anyString())).thenReturn(null);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyEntity);

        CompanyEntity result = companyService.createCompany(companyEntity);

        assertNotNull(result);
        assertEquals("Empresa Teste", result.getName());
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void testCreateCompany_AlreadyExists() {
        when(companyRepository.findByCnpj(anyString())).thenReturn(companyEntity);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            companyService.createCompany(companyEntity);
        });

        verify(companyRepository, never()).save(any(CompanyEntity.class));
    }

    @Test
    void testGetCompanyById_Success() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity));

        CompanyEntity result = companyService.getCompanyById(companyId);

        assertNotNull(result);
        assertEquals(companyId, result.getId());
        verify(companyRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void testGetCompanyById_NotFound() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.getCompanyById(companyId);
        });

        verify(companyRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void testGetCompanyByCNPJ_Success() {
        when(companyRepository.findByCnpj(anyString())).thenReturn(companyEntity);

        CompanyEntity result = companyService.getCompanyByCNPJ("123456789");

        assertNotNull(result);
        assertEquals("Empresa Teste", result.getName());
        verify(companyRepository, times(1)).findByCnpj(anyString());
    }

    @Test
    void testGetCompanyByCNPJ_NotFound() {
        when(companyRepository.findByCnpj(anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.getCompanyByCNPJ("123456789");
        });

        verify(companyRepository, times(1)).findByCnpj(anyString());
    }

    @Test
    void testUpdateCompany_Success() {
        CompanyEntity updatedCompany = new CompanyEntity();
        updatedCompany.setName("Novo Nome");
        updatedCompany.setAddress("Novo Endereço");
        updatedCompany.setPhone("Novo Telefone");

        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(updatedCompany);

        CompanyEntity result = companyService.updateCompany(companyId, updatedCompany);

        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void testUpdateCompany_NotFound() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        CompanyEntity updatedCompany = new CompanyEntity();

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.updateCompany(companyId, updatedCompany);
        });

        verify(companyRepository, never()).save(any(CompanyEntity.class));
    }

    @Test
    void testDeleteCompany_Success() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity));

        companyService.deleteCompany(companyId);

        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
        assertNotNull(companyEntity.getDeletedAt());
    }

    @Test
    void testDeleteCompany_NotFound() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.deleteCompany(companyId);
        });

        verify(companyRepository, never()).save(any(CompanyEntity.class));
    }

    @Test
    void testGetAllActiveCompanies() {
        List<CompanyEntity> companies = new ArrayList<>();
        companies.add(companyEntity);

        when(companyRepository.findByDeletedAtIsNull()).thenReturn(companies);

        List<CompanyEntity> result = companyService.getAllActiveCompanies();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(companyRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    void testGetAllInactiveCompanies() {
        List<CompanyEntity> companies = new ArrayList<>();
        companies.add(companyEntity);

        when(companyRepository.findByDeletedAtIsNotNull()).thenReturn(companies);

        List<CompanyEntity> result = companyService.getAllInactiveCompanies();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(companyRepository, times(1)).findByDeletedAtIsNotNull();
    }

    @Test
    void testGetCompanyKPIS_Success() {
        List<JobEntity> jobs = new ArrayList<>();
        JobEntity jobEntity = new JobEntity();
        jobEntity.setCandidates(new ArrayList<>());
        jobs.add(jobEntity);

        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity));
        when(jobRepository.findAllByCompanyEntity(any(CompanyEntity.class))).thenReturn(jobs);

        JobKPIDTO result = companyService.getCompanyKPIS(companyId);

        assertNotNull(result);
        assertEquals(1, result.getTotalJobs());
        verify(jobRepository, times(1)).findAllByCompanyEntity(any(CompanyEntity.class));
    }

    @Test
    void testGetCompanyKPIS_NotFound() {
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            companyService.getCompanyKPIS(companyId);
        });

        verify(jobRepository, never()).findAllByCompanyEntity(any(CompanyEntity.class));
    }
}

