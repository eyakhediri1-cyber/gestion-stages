package tn.gestionstages.backend.dto;

import tn.gestionstages.backend.entity.InternshipOffer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InternshipOfferResponse(
        Long id,
        Long companyId,
        String companyName,
        String title,
        String description,
        String domain,
        String location,
        String internshipType,
        String duration,
        LocalDate startDate,
        LocalDate applicationDeadline,
        String requiredSkills,
        boolean published,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static InternshipOfferResponse from(InternshipOffer offer) {
        return new InternshipOfferResponse(
                offer.getId(),
                offer.getCompany().getId(),
                offer.getCompany().getCompanyName(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getDomain(),
                offer.getLocation(),
                offer.getInternshipType(),
                offer.getDuration(),
                offer.getStartDate(),
                offer.getApplicationDeadline(),
                offer.getRequiredSkills(),
                offer.isPublished(),
                offer.getCreatedAt(),
                offer.getUpdatedAt()
        );
    }
}
