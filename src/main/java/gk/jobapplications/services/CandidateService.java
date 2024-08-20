package gk.jobapplications.services;

import java.time.LocalDateTime;

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

    public void deleteCandidate(CandidateEntity candidateEntity) {
        candidateEntity.setDeletedAt(LocalDateTime.now());
        candidateRepository.save(candidateEntity);

        
        throw new ResourceAlreadyExistsException("Candidato deletado com sucesso");
    }
}
