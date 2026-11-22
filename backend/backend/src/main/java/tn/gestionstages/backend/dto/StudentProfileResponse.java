package tn.gestionstages.backend.dto;

public record StudentProfileResponse(
    Long id,
    String fullName,
    String email,
    String university,
    String specialization,
    String studyLevel,
    String phone,
    String cvUrl,
    String linkedinUrl,
    String githubUrl
) {}
