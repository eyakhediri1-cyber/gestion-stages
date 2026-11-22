package tn.gestionstages.backend.dto;

import tn.gestionstages.backend.enums.ApplicationStatus;
import java.time.LocalDateTime;

public record ApplicationAdminResponse(
    Long id,
    Long studentId,
    String studentName,
    String studentEmail,
    Long offerId,
    String offerTitle,
    String companyName,
    String coverLetter,
    String cvUrl,
    ApplicationStatus status,
    LocalDateTime appliedAt,
    LocalDateTime updatedAt
) {}
