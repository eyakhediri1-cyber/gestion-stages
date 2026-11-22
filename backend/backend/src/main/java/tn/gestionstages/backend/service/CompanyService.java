package tn.gestionstages.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.gestionstages.backend.dto.CompanyProfileRequest;
import tn.gestionstages.backend.dto.CompanyProfileResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.entity.Company;
import tn.gestionstages.backend.enums.Role;
import tn.gestionstages.backend.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private Company getCompany(AppUser user) {

        if (user == null || user.getRole() != Role.COMPANY) {
            throw new AccessDeniedException(
                "Accès réservé aux entreprises"
            );
        }

        return companyRepository.findByUserId(user.getId())
            .orElseGet(() -> {
                Company company = new Company();
                company.setUser(user);
                company.setCompanyName(user.getFullName());
                return companyRepository.save(company);
            });
    }

    @Transactional
    public CompanyProfileResponse getProfile(AppUser user) {
        return toResponse(getCompany(user));
    }

    @Transactional
    public CompanyProfileResponse updateProfile(
        AppUser user,
        CompanyProfileRequest request
    ) {

        Company company = getCompany(user);

        if (request.companyName() != null
                && !request.companyName().isBlank()) {
            company.setCompanyName(request.companyName());
        }

        company.setIndustry(request.industry());
        company.setDescription(request.description());
        company.setWebsite(request.website());
        company.setAddress(request.address());
        company.setPhone(request.phone());
        company.setLogoUrl(request.logoUrl());

        return toResponse(companyRepository.save(company));
    }

    private CompanyProfileResponse toResponse(Company company) {

        return new CompanyProfileResponse(
            company.getId(),
            company.getCompanyName(),
            company.getIndustry(),
            company.getDescription(),
            company.getWebsite(),
            company.getAddress(),
            company.getPhone(),
            company.getLogoUrl(),
            company.isVerified()
        );
    }
}
