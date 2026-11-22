package tn.gestionstages.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import tn.gestionstages.backend.dto.CompanyProfileRequest;
import tn.gestionstages.backend.dto.CompanyProfileResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/me")
    public CompanyProfileResponse getMyProfile(
        @AuthenticationPrincipal AppUser user
    ) {
        return companyService.getProfile(user);
    }

    @PutMapping("/me")
    public CompanyProfileResponse updateMyProfile(
        @AuthenticationPrincipal AppUser user,
        @RequestBody CompanyProfileRequest request
    ) {
        return companyService.updateProfile(user, request);
    }
}
