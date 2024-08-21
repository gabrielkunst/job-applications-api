package gk.jobapplications.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.exceptions.ResourceNotFoundException;
import gk.jobapplications.repositories.CandidateRepository;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateEntity createCandidate(CandidateEntity candidateEntity) {
        CandidateEntity candidateFromDB = candidateRepository.findByEmail(candidateEntity.getEmail());

        if (candidateFromDB != null) {
            throw new ResourceAlreadyExistsException("E-mail já cadastrado");
        }

        candidateEntity.setDeletedAt(null);

        return candidateRepository.save(candidateEntity);
    }

    public void deleteCandidate(UUID id) {
        
        CandidateEntity candidateFromDB = candidateRepository.findById(id).orElse(null);

        if (candidateFromDB == null) {
            throw new ResourceNotFoundException("Candidato não encontrado");
        }

        candidateFromDB.setDeletedAt(LocalDateTime.now());

        candidateRepository.save(candidateFromDB);

    }

    public CandidateEntity updateCandidate(UUID id, CandidateEntity candidateEntity) {
        CandidateEntity candidateFromDB = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato não encontrado"));

        if (candidateEntity.getName() != null) {
            candidateFromDB.setName(candidateEntity.getName());
        }
        if (candidateEntity.getEmail() != null) {
            candidateFromDB.setEmail(candidateEntity.getEmail());
        }
        if (candidateEntity.getProfession() != null) {
            candidateFromDB.setProfession(candidateEntity.getProfession());
        }
        if (candidateEntity.getPasswordHash() != null) {
            candidateFromDB.setPasswordHash(candidateEntity.getPasswordHash());
        }

        return candidateRepository.save(candidateFromDB);
    }

    public List<CandidateEntity> getAllActiveCandidates() {
        return candidateRepository.findByDeletedAtIsNull();
    }

    public List<CandidateEntity> getAllInactiveCandidates() {
        return candidateRepository.findByDeletedAtIsNotNull();
    }

    
}
