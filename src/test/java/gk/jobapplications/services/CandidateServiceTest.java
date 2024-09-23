
package gk.jobapplications.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

class CandidateServiceTest {

    @InjectMocks
    private CandidateService candidateService;

    @Mock
    private CandidateRepository candidateRepository;

    private CandidateEntity candidateEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        candidateEntity = new CandidateEntity();
        candidateEntity.setId(UUID.randomUUID());
        candidateEntity.setName("João Silva");
        candidateEntity.setEmail("joao.silva@example.com");
        candidateEntity.setProfession("Desenvolvedor");
        candidateEntity.setPasswordHash("hashedPassword");
        candidateEntity.setDeletedAt(null);
    }

    @Test
    void testCreateCandidate_Success() {
        when(candidateRepository.findByEmail(any(String.class))).thenReturn(null);
        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(candidateEntity);

        CandidateEntity createdCandidate = candidateService.createCandidate(candidateEntity);

        assertEquals("João Silva", createdCandidate.getName());
        assertEquals("joao.silva@example.com", createdCandidate.getEmail());
        assertNull(createdCandidate.getDeletedAt());
    }

    @Test
    void testCreateCandidate_EmailAlreadyExists() {
        when(candidateRepository.findByEmail(any(String.class))).thenReturn(candidateEntity);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            candidateService.createCandidate(candidateEntity);
        });
    }

    @Test
    void testDeleteCandidate_Success() {
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.of(candidateEntity));
        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(candidateEntity);

        candidateService.deleteCandidate(candidateEntity.getId());

        assertNotNull(candidateEntity.getDeletedAt());
    }

    @Test
    void testDeleteCandidate_NotFound() {
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            candidateService.deleteCandidate(candidateEntity.getId());
        });
    }

    @Test
    void testUpdateCandidate_Success() {
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.of(candidateEntity));
        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(candidateEntity);

        CandidateEntity updatedCandidate = new CandidateEntity();
        updatedCandidate.setName("Maria Oliveira");

        CandidateEntity result = candidateService.updateCandidate(candidateEntity.getId(), updatedCandidate);

        assertEquals("Maria Oliveira", result.getName());
        assertEquals("joao.silva@example.com", result.getEmail());
    }

    @Test
    void testUpdateCandidate_NotFound() {
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            candidateService.updateCandidate(candidateEntity.getId(), candidateEntity);
        });
    }

    @Test
    void testGetAllActiveCandidates() {
        when(candidateRepository.findByDeletedAtIsNull()).thenReturn(Collections.singletonList(candidateEntity));

        var candidates = candidateService.getAllActiveCandidates();

        assertEquals(1, candidates.size());
        assertEquals("João Silva", candidates.get(0).getName());
    }

    @Test
    void testGetAllInactiveCandidates() {
        candidateEntity.setDeletedAt(LocalDateTime.now());
        when(candidateRepository.findByDeletedAtIsNotNull()).thenReturn(Collections.singletonList(candidateEntity));

        var candidates = candidateService.getAllInactiveCandidates();

        assertEquals(1, candidates.size());
        assertEquals("João Silva", candidates.get(0).getName());
    }

    @Test
    void testGetCandidateById_Success() {
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.of(candidateEntity));

        CandidateEntity foundCandidate = candidateService.getCandidateById(candidateEntity.getId());

        assertEquals("João Silva", foundCandidate.getName());
    }

    @Test
    void testGetCandidateById_NotFound() {
        when(candidateRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            candidateService.getCandidateById(candidateEntity.getId());
        });
    }
}
