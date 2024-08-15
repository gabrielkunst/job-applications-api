package gk.jobapplications.services;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        CompanyEntity companyFromDB = companyRepository.findByCNPJ(companyEntity.getCnpj());

        if (companyFromDB != null) {
            throw new ResourceAlreadyExistsException("CNPJ já cadastrado");
        }

        return companyRepository.save(companyEntity);
    }
}
