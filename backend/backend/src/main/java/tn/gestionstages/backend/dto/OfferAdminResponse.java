package tn.gestionstages.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OfferAdminResponse(
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
) {}
