package tn.gestionstages.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.gestionstages.backend.dto.AdminDashboardResponse;
import tn.gestionstages.backend.dto.ApplicationAdminResponse;
import tn.gestionstages.backend.dto.ApplicationStatusUpdateRequest;
import tn.gestionstages.backend.dto.CompanyAdminResponse;
import tn.gestionstages.backend.dto.OfferAdminResponse;
import tn.gestionstages.backend.dto.UserAdminResponse;
import tn.gestionstages.backend.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard() {
        return adminService.getDashboard();
    }

    @GetMapping("/users")
    public List<UserAdminResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserAdminResponse getUserById(
            @PathVariable Long id) {
        return adminService.getUserById(id);
    }

    @PatchMapping("/users/{id}/disable")
    public UserAdminResponse disableUser(
            @PathVariable Long id) {
        return adminService.disableUser(id);
    }

    @PatchMapping("/users/{id}/enable")
    public UserAdminResponse enableUser(
            @PathVariable Long id) {
        return adminService.enableUser(id);
    }

    @GetMapping("/companies")
    public List<CompanyAdminResponse> getAllCompanies() {
        return adminService.getAllCompanies();
    }

    @GetMapping("/companies/{id}")
    public CompanyAdminResponse getCompanyById(
            @PathVariable Long id) {
        return adminService.getCompanyById(id);
    }

    @PatchMapping("/companies/{id}/verify")
    public CompanyAdminResponse verifyCompany(
            @PathVariable Long id) {
        return adminService.verifyCompany(id);
    }

    @PatchMapping("/companies/{id}/unverify")
    public CompanyAdminResponse unverifyCompany(
            @PathVariable Long id) {
        return adminService.unverifyCompany(id);
    }

    @GetMapping("/offers")
    public List<OfferAdminResponse> getAllOffers() {
        return adminService.getAllOffers();
    }

    @GetMapping("/offers/{id}")
    public OfferAdminResponse getOfferById(
            @PathVariable Long id) {
        return adminService.getOfferById(id);
    }

    @PatchMapping("/offers/{id}/publish")
    public OfferAdminResponse publishOffer(
            @PathVariable Long id) {
        return adminService.publishOffer(id);
    }

    @PatchMapping("/offers/{id}/unpublish")
    public OfferAdminResponse unpublishOffer(
            @PathVariable Long id) {
        return adminService.unpublishOffer(id);
    }

    @DeleteMapping("/offers/{id}")
    public void deleteOffer(@PathVariable Long id) {
        adminService.deleteOffer(id);
    }

    @GetMapping("/applications")
    public List<ApplicationAdminResponse> getAllApplications() {
        return adminService.getAllApplications();
    }

    @GetMapping("/applications/{id}")
    public ApplicationAdminResponse getApplicationById(
            @PathVariable Long id) {
        return adminService.getApplicationById(id);
    }

    @PatchMapping("/applications/{id}/status")
    public ApplicationAdminResponse updateApplicationStatus(
            @PathVariable Long id,
            @jakarta.validation.Valid
            @RequestBody ApplicationStatusUpdateRequest request) {

        return adminService.updateApplicationStatus(
                id,
                request.status()
        );
    }
}
