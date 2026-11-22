package tn.gestionstages.backend.controller;

import tn.gestionstages.backend.dto.InternshipApplicationRequest;
import tn.gestionstages.backend.dto.InternshipApplicationResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.entity.InternshipApplication;
import tn.gestionstages.backend.enums.ApplicationStatus;
import tn.gestionstages.backend.enums.Role;
import tn.gestionstages.backend.service.InternshipApplicationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class InternshipApplicationController {

    private final InternshipApplicationService applicationService;

    public InternshipApplicationController(
            InternshipApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    private void requireRole(AppUser user, Role role) {
        if (user.getRole() != role) {
            throw new AccessDeniedException(
                    "You are not allowed to perform this action");
        }
    }

    // Student applies to an internship offer
    @PostMapping("/{offerId}")
    public ResponseEntity<InternshipApplicationResponse> apply(
            @PathVariable Long offerId,
            @AuthenticationPrincipal AppUser user,
            @Valid @RequestBody InternshipApplicationRequest request) {

        requireRole(user, Role.STUDENT);

        InternshipApplication application =
                applicationService.apply(user.getId(), offerId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InternshipApplicationResponse.from(application));
    }

    // Student views their own applications
    @GetMapping("/my")
    public ResponseEntity<List<InternshipApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal AppUser user) {

        requireRole(user, Role.STUDENT);

        List<InternshipApplicationResponse> applications =
                applicationService.getStudentApplications(user.getId())
                        .stream()
                        .map(InternshipApplicationResponse::from)
                        .toList();

        return ResponseEntity.ok(applications);
    }

    // Student withdraws their own application
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<InternshipApplicationResponse> withdraw(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser user) {

        requireRole(user, Role.STUDENT);

        InternshipApplication application =
                applicationService.withdraw(id, user.getId());

        return ResponseEntity.ok(
                InternshipApplicationResponse.from(application));
    }

    // Company views applications received for one of its offers
    @GetMapping("/offer/{offerId}")
    public ResponseEntity<List<InternshipApplicationResponse>> getOfferApplications(
            @PathVariable Long offerId,
            @AuthenticationPrincipal AppUser user) {

        requireRole(user, Role.COMPANY);

        List<InternshipApplicationResponse> applications =
                applicationService.getOfferApplications(
                        offerId, user.getId())
                        .stream()
                        .map(InternshipApplicationResponse::from)
                        .toList();

        return ResponseEntity.ok(applications);
    }

    // Company updates the status of an application
    @PatchMapping("/{id}/status")
    public ResponseEntity<InternshipApplicationResponse> updateStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser user,
            @RequestParam ApplicationStatus status) {

        requireRole(user, Role.COMPANY);

        InternshipApplication application =
                applicationService.updateStatus(
                        id, user.getId(), status);

        return ResponseEntity.ok(
                InternshipApplicationResponse.from(application));
    }
}
