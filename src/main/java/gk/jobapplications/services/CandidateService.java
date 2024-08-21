package gk.jobapplications.services;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
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
            throw new RuntimeException("Candidato não encontrado");
        }

        candidateFromDB.setDeletedAt(LocalDateTime.now());

        candidateRepository.save(candidateFromDB);

    }
}
