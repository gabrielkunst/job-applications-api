package gk.jobapplications.services;

import gk.jobapplications.entities.CompanyEntity;
import gk.jobapplications.exceptions.ResourceAlreadyExistsException;
import gk.jobapplications.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        CompanyEntity companyFromDB = companyRepository.findByCnpj(companyEntity.getCnpj());

        if (companyFromDB != null) {
            throw new ResourceAlreadyExistsException("CNPJ já cadastrado");
        }

        return companyRepository.save(companyEntity);
    }

    public CompanyEntity getCompanyById(UUID id) {
        CompanyEntity companyEntity = companyRepository.findById(id).orElse(null);

        if (companyEntity == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        return companyEntity;
    }

    public CompanyEntity getCompanyByCNPJ(String cnpj) {
        CompanyEntity companyEntity = companyRepository.findByCnpj(cnpj);

        if (companyEntity == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        return companyEntity;
    }

    public CompanyEntity updateCompany(UUID id, CompanyEntity companyEntity) {
        CompanyEntity companyFromDB = companyRepository.findById(id).orElse(null);

        if (companyFromDB == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        companyFromDB.setName(companyEntity.getName());
        companyFromDB.setAddress(companyEntity.getAddress());
        companyFromDB.setPhone(companyEntity.getPhone());

        return companyRepository.save(companyFromDB);
    }

    public void deleteCompany(UUID id) {
        CompanyEntity companyEntity = companyRepository.findById(id).orElse(null);

        if (companyEntity == null) {
            throw new ResourceNotFoundException("Empresa não encontrada");
        }

        companyRepository.delete(companyEntity);
    }
}
