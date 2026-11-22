package tn.gestionstages.backend.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import tn.gestionstages.backend.dto.InternshipOfferRequest;
import tn.gestionstages.backend.entity.Company;
import tn.gestionstages.backend.entity.InternshipOffer;
import tn.gestionstages.backend.repository.CompanyRepository;
import tn.gestionstages.backend.repository.InternshipOfferRepository;

@Service
@Transactional
public class InternshipOfferService {

    private final InternshipOfferRepository offerRepository;
    private final CompanyRepository companyRepository;

    public InternshipOfferService(
            InternshipOfferRepository offerRepository,
            CompanyRepository companyRepository) {
        this.offerRepository = offerRepository;
        this.companyRepository = companyRepository;
    }

    // Retrieve the company associated with the authenticated user
    private Company getCompany(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Company profile not found"));
    }

    // Create an internship offer
    public InternshipOffer createOffer(
            Long userId,
            InternshipOfferRequest request) {

        Company company = getCompany(userId);

        InternshipOffer offer = new InternshipOffer();

        offer.setCompany(company);
        updateOfferFields(offer, request);

        return offerRepository.save(offer);
    }

    // Get all published offers
    @Transactional(readOnly = true)
    public List<InternshipOffer> getPublishedOffers() {
        return offerRepository.findByPublishedTrue();
    }

    // Get all offers belonging to the authenticated company
    @Transactional(readOnly = true)
    public List<InternshipOffer> getCompanyOffers(Long userId) {
        return offerRepository.findByCompanyUserId(userId);
    }

    // Get one offer belonging to the authenticated company
    @Transactional(readOnly = true)
    public InternshipOffer getCompanyOffer(Long offerId, Long userId) {
        return findOwnedOffer(offerId, userId);
    }

    // Update an existing offer
    public InternshipOffer updateOffer(
            Long offerId,
            Long userId,
            InternshipOfferRequest request) {

        InternshipOffer offer = findOwnedOffer(offerId, userId);

        updateOfferFields(offer, request);

        return offerRepository.save(offer);
    }

    // Publish or unpublish an offer
    public InternshipOffer setPublished(
            Long offerId,
            Long userId,
            boolean published) {

        InternshipOffer offer = findOwnedOffer(offerId, userId);

        offer.setPublished(published);

        return offerRepository.save(offer);
    }

    // Delete an offer
    public void deleteOffer(Long offerId, Long userId) {

        InternshipOffer offer = findOwnedOffer(offerId, userId);

        offerRepository.delete(offer);
    }

    // Verify ownership before allowing any modification
    private InternshipOffer findOwnedOffer(Long offerId, Long userId) {

        InternshipOffer offer = offerRepository.findById(offerId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Internship offer not found"));

        if (!offer.getCompany().getUser().getId().equals(userId)) {
            throw new AccessDeniedException(
                    "You are not allowed to manage this offer");
        }

        return offer;
    }

    // Map request fields to the entity
    private void updateOfferFields(
            InternshipOffer offer,
            InternshipOfferRequest request) {

        offer.setTitle(request.title());
        offer.setDescription(request.description());
        offer.setDomain(request.domain());
        offer.setLocation(request.location());
        offer.setInternshipType(request.internshipType());
        offer.setDuration(request.duration());
        offer.setStartDate(request.startDate());
        offer.setApplicationDeadline(request.applicationDeadline());
        offer.setRequiredSkills(request.requiredSkills());
    }

    @Transactional(readOnly = true)
    public InternshipOffer getPublishedOffer(Long offerId) {
        InternshipOffer offer = offerRepository.findById(offerId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Internship offer not found"));

        if (!offer.isPublished()) {
            throw new EntityNotFoundException("Internship offer not found");
        }

        return offer;
    }
}
