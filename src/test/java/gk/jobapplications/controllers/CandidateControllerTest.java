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

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.services.CandidateService;

@WebMvcTest(CandidateController.class)
class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCandidate_ShouldReturnCreatedResponse() throws Exception {
        CandidateEntity candidateEntity = CandidateEntity.builder()
                .id(UUID.randomUUID())
                .name("João Silva")
                .email("joao.silva@example.com")
                .build();

        when(candidateService.createCandidate(any(CandidateEntity.class))).thenReturn(candidateEntity);

        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"João Silva\", \"email\":\"joao.silva@example.com\", \"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Cadidato criado com sucesso"));
    }

    @Test
    void deleteCandidate_ShouldReturnNoContentResponse() throws Exception {
        UUID candidateId = UUID.randomUUID();

        doNothing().when(candidateService).deleteCandidate(candidateId);

        mockMvc.perform(delete("/api/candidates/{id}", candidateId))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCandidate_ShouldReturnOkResponse() throws Exception {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity updatedCandidate = CandidateEntity.builder()
                .id(candidateId)
                .name("Maria Oliveira")
                .email("maria.oliveira@example.com")
                .build();

        when(candidateService.updateCandidate(eq(candidateId), any(CandidateEntity.class))).thenReturn(updatedCandidate);

        mockMvc.perform(put("/api/candidates/{id}", candidateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Maria Oliveira\", \"email\":\"maria.oliveira@example.com\", \"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Candidato atualizado com sucesso"));
    }

    @Test
    void getAllActiveCandidates_ShouldReturnOkResponse() throws Exception {
        List<CandidateEntity> candidates = new ArrayList<>();
        candidates.add(CandidateEntity.builder().name("João Silva").email("joao.silva@example.com").build());

        when(candidateService.getAllActiveCandidates()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de candidatos ativos"));
    }

    @Test
    void getAllInactiveCandidates_ShouldReturnOkResponse() throws Exception {
        List<CandidateEntity> candidates = new ArrayList<>();
        candidates.add(CandidateEntity.builder().name("Maria Oliveira").email("maria.oliveira@example.com").build());

        when(candidateService.getAllInactiveCandidates()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates/inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de candidatos inativos"));
    }

    @Test
    void getCandidateById_ShouldReturnOkResponse() throws Exception {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = CandidateEntity.builder()
                .id(candidateId)
                .name("Ana Costa")
                .email("ana.costa@example.com")
                .build();

        when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);

        mockMvc.perform(get("/api/candidates/{id}", candidateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Candidato encontrado"));
    }
}
