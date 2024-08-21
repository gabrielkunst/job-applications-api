package gk.jobapplications.controllers;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.responses.ApiResponse;
import gk.jobapplications.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyEntity>> createCompany(@RequestBody CompanyEntity companyEntity) {
        CompanyEntity company = companyService.createCompany(companyEntity);
        ApiResponse<CompanyEntity> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Empresa criada com sucesso",
                company
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyEntity>> getCompanyById(@PathVariable UUID id) {
        CompanyEntity company = companyService.getCompanyById(id);
        ApiResponse<CompanyEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Empresa encontrada com sucesso",
                company
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyEntity>> updateCompany(@PathVariable UUID id, @RequestBody CompanyEntity companyEntity) {
        CompanyEntity company = companyService.updateCompany(id, companyEntity);
        ApiResponse<CompanyEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Empresa atualizada com sucesso",
                company
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
