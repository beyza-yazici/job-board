package com.jobboard.service;

import com.jobboard.entity.Company;
import com.jobboard.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    
    private final CompanyRepository companyRepository;
    
    public Company createCompany(Company company) {
        if (companyRepository.existsByUserId(company.getUser().getId())) {
            throw new RuntimeException("Company profile already exists for this user");
        }
        return companyRepository.save(company);
    }
    
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }
    
    public Optional<Company> getCompanyByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }
    
    public Page<Company> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }
    
    public Company updateCompany(Long id, Company updatedCompany) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        company.setCompanyName(updatedCompany.getCompanyName());
        company.setDescription(updatedCompany.getDescription());
        company.setWebsite(updatedCompany.getWebsite());
        company.setLogoUrl(updatedCompany.getLogoUrl());
        
        return companyRepository.save(company);
    }
    
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}