package gk.jobapplications.services;

import org.springframework.beans.factory.annotation.Autowired;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.repositories.CandidateRepository;

public class CandidateService {
  
  @Autowired
  private CandidateRepository candidateRepository;

  public CandidateEntity createCandidate(CandidateEntity candidateEntity) {
        CandidateEntity candidateFromDB = candidateRepository.findByEmail(candidateEntity.getEmail());

        if (candidateFromDB != null) {
            throw new ResourceAlreadyExistsException("E-mail já cadastrado");
        }

        return candidateRepository.save(candidateEntity);
    }
}
