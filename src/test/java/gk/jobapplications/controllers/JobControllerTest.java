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

import gk.jobapplications.dtos.CreateJobDTO;
import gk.jobapplications.entities.JobEntity;
import gk.jobapplications.responses.ApiResponse;
import gk.jobapplications.services.JobService;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createJob_ShouldReturnCreatedResponse() throws Exception {
        UUID companyId = UUID.randomUUID();
        CreateJobDTO createJobDTO = CreateJobDTO.builder()
                .title("Desenvolvedor Backend")
                .description("Responsável pelo desenvolvimento de APIs")
                .quantity(1)
                .companyId(companyId)
                .build();

        JobEntity jobEntity = JobEntity.builder()
                .id(UUID.randomUUID())
                .title("Desenvolvedor Backend")
                .description("Responsável pelo desenvolvimento de APIs")
                .quantity(1)
                .build();

        when(jobService.createJob(any(JobEntity.class))).thenReturn(jobEntity);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Desenvolvedor Backend\", \"description\":\"Responsável pelo desenvolvimento de APIs\", \"quantity\":1, \"companyId\":\"" + companyId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Vaga criada com sucesso"));
    }

    @Test
    void getAllJobs_ShouldReturnOkResponse() throws Exception {
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(JobEntity.builder().build());

        when(jobService.getAllJobs()).thenReturn(jobs);

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vagas retornadas com sucesso"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getJobsByCompany_ShouldReturnOkResponse() throws Exception {
        UUID companyId = UUID.randomUUID();
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(JobEntity.builder().build());

        when(jobService.getJobsByCompany(companyId)).thenReturn(jobs);

        mockMvc.perform(get("/api/jobs/company/{companyId}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vagas retornadas com sucesso"));
    }

    @Test
    void getJobById_ShouldReturnOkResponse() throws Exception {
        UUID jobId = UUID.randomUUID();
        JobEntity jobEntity = JobEntity.builder()
                .id(jobId)
                .title("Desenvolvedor Frontend")
                .description("Responsável pela interface do usuário")
                .build();

        when(jobService.getJobById(jobId)).thenReturn(jobEntity);

        mockMvc.perform(get("/api/jobs/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vaga retornada com sucesso"));
    }

    @Test
    void deleteJob_ShouldReturnNoContentResponse() throws Exception {
        UUID jobId = UUID.randomUUID();

        doNothing().when(jobService).deleteJob(jobId);

        mockMvc.perform(delete("/api/jobs/{id}", jobId))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateJob_ShouldReturnOkResponse() throws Exception {
        UUID jobId = UUID.randomUUID();
        CreateJobDTO createJobDTO = CreateJobDTO.builder()
                .title("Desenvolvedor Fullstack")
                .description("Desenvolvimento de aplicações completas")
                .quantity(2)
                .companyId(UUID.randomUUID())
                .build();

        JobEntity updatedJobEntity = JobEntity.builder()
                .id(jobId)
                .title("Desenvolvedor Fullstack")
                .build();

        when(jobService.updateJob(eq(jobId), any(JobEntity.class))).thenReturn(updatedJobEntity);

        mockMvc.perform(put("/api/jobs/{id}", jobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Desenvolvedor Fullstack\", \"description\":\"Desenvolvimento de aplicações completas\", \"quantity\":2, \"companyId\":\"" + createJobDTO.getCompanyId() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vaga atualizada com sucesso"));
    }

    @Test
    void applyToJob_ShouldReturnOkResponse() throws Exception {
        UUID jobId = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();
        JobEntity jobEntity = JobEntity.builder()
                .id(jobId)
                .title("Desenvolvedor Backend")
                .build();

        when(jobService.applyToJob(jobId, candidateId)).thenReturn(jobEntity);

        mockMvc.perform(post("/api/jobs/{jobId}/apply/{candidateId}", jobId, candidateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Candidatura realizada com sucesso"));
    }

    @Test
    void getAllActiveJob_ShouldReturnOkResponse() throws Exception {
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(JobEntity.builder().build());

        when(jobService.getAllActiveJob()).thenReturn(jobs);

        mockMvc.perform(get("/api/jobs/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de vagas de emprego ativas"));
    }

    @Test
    void getAllInactiveJob_ShouldReturnOkResponse() throws Exception {
        List<JobEntity> jobs = new ArrayList<>();
        jobs.add(JobEntity.builder().build());

        when(jobService.getAllInactiveJob()).thenReturn(jobs);

        mockMvc.perform(get("/api/jobs/inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de vagas de emprego inativas"));
    }
}
