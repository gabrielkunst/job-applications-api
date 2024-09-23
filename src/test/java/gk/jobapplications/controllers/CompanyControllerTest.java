package gk.jobapplications.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import gk.jobapplications.dtos.JobKPIDTO;
import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.services.CompanyService;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCompany_ShouldReturnCreatedResponse() throws Exception {
        CompanyEntity companyEntity = CompanyEntity.builder()
                .id(UUID.randomUUID())
                .name("Tech Solutions")
                .build();

        when(companyService.createCompany(any(CompanyEntity.class))).thenReturn(companyEntity);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Tech Solutions\", \"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Empresa criada com sucesso"));
    }

    @Test
    void getCompanyById_ShouldReturnOkResponse() throws Exception {
        UUID companyId = UUID.randomUUID();
        CompanyEntity company = CompanyEntity.builder()
                .id(companyId)
                .name("Tech Solutions")
                .build();

        when(companyService.getCompanyById(companyId)).thenReturn(company);

        mockMvc.perform(get("/api/companies/{id}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Empresa encontrada com sucesso"));
    }

    @Test
    void getCompanyKPIS_ShouldReturnOkResponse() throws Exception {
        UUID companyId = UUID.randomUUID();
        JobKPIDTO jobKPIDTO = new JobKPIDTO();

        when(companyService.getCompanyKPIS(companyId)).thenReturn(jobKPIDTO);

        mockMvc.perform(get("/api/companies/{id}/kpis", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("KPIs da empresa encontrados com sucesso"));
    }

    @Test
    void updateCompany_ShouldReturnOkResponse() throws Exception {
        UUID companyId = UUID.randomUUID();
        CompanyEntity updatedCompany = CompanyEntity.builder()
                .id(companyId)
                .name("Tech Innovations")
                .build();

        when(companyService.updateCompany(eq(companyId), any(CompanyEntity.class))).thenReturn(updatedCompany);

        mockMvc.perform(put("/api/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Tech Innovations\", \"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Empresa atualizada com sucesso"));
    }

    @Test
    void deleteCompany_ShouldReturnNoContentResponse() throws Exception {
        UUID companyId = UUID.randomUUID();

        doNothing().when(companyService).deleteCompany(companyId);

        mockMvc.perform(delete("/api/companies/{id}", companyId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllActiveCompanies_ShouldReturnOkResponse() throws Exception {
        List<CompanyEntity> companies = new ArrayList<>();
        companies.add(CompanyEntity.builder().name("Tech Solutions").build());

        when(companyService.getAllActiveCompanies()).thenReturn(companies);

        mockMvc.perform(get("/api/companies/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de empresas ativas"));
    }

    @Test
    void getAllInactiveCompanies_ShouldReturnOkResponse() throws Exception {
        List<CompanyEntity> companies = new ArrayList<>();
        companies.add(CompanyEntity.builder().name("Old Tech").build());

        when(companyService.getAllInactiveCompanies()).thenReturn(companies);

        mockMvc.perform(get("/api/companies/inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de empresas inativas"));
    }
}

