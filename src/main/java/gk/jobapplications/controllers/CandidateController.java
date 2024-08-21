package gk.jobapplications.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gk.jobapplications.entities.CandidateEntity;
import gk.jobapplications.responses.ApiResponse;
import gk.jobapplications.services.CandidateService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<ApiResponse<CandidateEntity>> createCandidate(@RequestBody CandidateEntity candidateEntity) {
        CandidateEntity candidate = candidateService.createCandidate(candidateEntity);
        ApiResponse<CandidateEntity> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Cadidato criado com sucesso",
                candidate
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
        
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CandidateEntity>> updateCandidate(@PathVariable UUID id, @RequestBody CandidateEntity candidateEntity) {
        CandidateEntity candidate = candidateService.updateCandidate(id, candidateEntity);
        ApiResponse<CandidateEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Candidato atualizado com sucesso",
                candidate
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CandidateEntity>>> getAllActiveCandidates() {
        List<CandidateEntity> candidates = candidateService.getAllActiveCandidates();
        ApiResponse<List<CandidateEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de candidatos ativos",
                candidates
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<List<CandidateEntity>>> getAllInactiveCandidates() {
        List<CandidateEntity> candidates = candidateService.getAllInactiveCandidates();
        ApiResponse<List<CandidateEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de candidatos inativos",
                candidates
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
