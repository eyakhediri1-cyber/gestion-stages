package tn.gestionstages.backend.controller;

import tn.gestionstages.backend.dto.InternshipOfferRequest;
import tn.gestionstages.backend.dto.InternshipOfferResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.entity.InternshipOffer;
import tn.gestionstages.backend.enums.Role;
import tn.gestionstages.backend.service.InternshipOfferService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class InternshipOfferController {

    private final InternshipOfferService offerService;

    public InternshipOfferController(InternshipOfferService offerService) {
        this.offerService = offerService;
    }

    private void requireCompany(AppUser user) {
        if (user.getRole() != Role.COMPANY) {
            throw new AccessDeniedException(
                    "Only companies can manage internship offers");
        }
    }

    // Create an offer
    @PostMapping
    public ResponseEntity<InternshipOfferResponse> createOffer(
            @AuthenticationPrincipal AppUser user,
            @Valid @RequestBody InternshipOfferRequest request) {

        requireCompany(user);

        InternshipOffer offer =
                offerService.createOffer(user.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InternshipOfferResponse.from(offer));
    }

    // Get all published offers
    @GetMapping
    public ResponseEntity<List<InternshipOfferResponse>> getPublishedOffers() {

        List<InternshipOfferResponse> offers =
                offerService.getPublishedOffers()
                        .stream()
                        .map(InternshipOfferResponse::from)
                        .toList();

        return ResponseEntity.ok(offers);
    }

    // Get offers belonging to the authenticated company
    @GetMapping("/my")
    public ResponseEntity<List<InternshipOfferResponse>> getMyOffers(
            @AuthenticationPrincipal AppUser user) {

        requireCompany(user);

        List<InternshipOfferResponse> offers =
                offerService.getCompanyOffers(user.getId())
                        .stream()
                        .map(InternshipOfferResponse::from)
                        .toList();

        return ResponseEntity.ok(offers);
    }

    // Get one published offer
    @GetMapping("/{id}")
    public ResponseEntity<InternshipOfferResponse> getOffer(
            @PathVariable Long id) {

        InternshipOffer offer = offerService.getPublishedOffer(id);

        return ResponseEntity.ok(
                InternshipOfferResponse.from(offer));
    }

    // Update an offer
    @PutMapping("/{id}")
    public ResponseEntity<InternshipOfferResponse> updateOffer(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser user,
            @Valid @RequestBody InternshipOfferRequest request) {

        requireCompany(user);

        InternshipOffer offer =
                offerService.updateOffer(id, user.getId(), request);

        return ResponseEntity.ok(
                InternshipOfferResponse.from(offer));
    }

    // Publish or unpublish an offer
    @PatchMapping("/{id}/publish")
    public ResponseEntity<InternshipOfferResponse> setPublished(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser user,
            @RequestParam boolean published) {

        requireCompany(user);

        InternshipOffer offer =
                offerService.setPublished(id, user.getId(), published);

        return ResponseEntity.ok(
                InternshipOfferResponse.from(offer));
    }

    // Delete an offer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser user) {

        requireCompany(user);

        offerService.deleteOffer(id, user.getId());

        return ResponseEntity.noContent().build();
    }
}
