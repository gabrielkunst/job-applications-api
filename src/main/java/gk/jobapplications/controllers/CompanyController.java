package gk.jobapplications.controllers;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyEntity> createCompany(@RequestBody CompanyEntity companyEntity) {
        CompanyEntity company = companyService.createCompany(companyEntity);
        return new ResponseEntity<CompanyEntity>(company, HttpStatus.CREATED);
    }
}
