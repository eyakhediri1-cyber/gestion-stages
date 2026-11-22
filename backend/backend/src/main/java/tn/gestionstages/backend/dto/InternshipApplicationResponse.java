package tn.gestionstages.backend.dto;

import tn.gestionstages.backend.entity.InternshipApplication;
import tn.gestionstages.backend.enums.ApplicationStatus;

import java.time.LocalDateTime;

public record InternshipApplicationResponse(

        Long id,
        Long offerId,
        String offerTitle,
        Long studentId,
        String studentName,
        String studentEmail,
        String university,
        String specialization,
        String studyLevel,
        String coverLetter,
        String cvUrl,
        ApplicationStatus status,
        LocalDateTime appliedAt,
        LocalDateTime updatedAt

) {
    public static InternshipApplicationResponse from(
            InternshipApplication application) {

        return new InternshipApplicationResponse(
                application.getId(),
                application.getOffer().getId(),
                application.getOffer().getTitle(),
                application.getStudent().getId(),
                application.getStudent().getUser().getFullName(),
                application.getStudent().getUser().getEmail(),
                application.getStudent().getUniversity(),
                application.getStudent().getSpecialization(),
                application.getStudent().getStudyLevel(),
                application.getCoverLetter(),
                application.getCvUrl(),
                application.getStatus(),
                application.getAppliedAt(),
                application.getUpdatedAt()
        );
    }
}
