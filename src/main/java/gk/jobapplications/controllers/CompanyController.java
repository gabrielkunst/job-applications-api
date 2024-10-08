package gk.jobapplications.controllers;

import java.util.List;
import java.util.UUID;

import gk.jobapplications.dtos.JobKPIDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.responses.ApiResponse;
import gk.jobapplications.services.CompanyService;

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

    @GetMapping("/{id}/kpis")
    public ResponseEntity<ApiResponse<JobKPIDTO>> getCompanyKPIS(@PathVariable UUID id) {
        JobKPIDTO jobKPIDTO = companyService.getCompanyKPIS(id);
        ApiResponse<JobKPIDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "KPIs da empresa encontrados com sucesso",
                jobKPIDTO
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

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CompanyEntity>>> getAllActiveCompanies() {
        List<CompanyEntity> companies = companyService.getAllActiveCompanies();
        ApiResponse<List<CompanyEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de empresas ativas",
                companies
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<List<CompanyEntity>>> getAllInactiveCompanies() {
        List<CompanyEntity> companies = companyService.getAllInactiveCompanies();
        ApiResponse<List<CompanyEntity>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de empresas inativas",
                companies
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
