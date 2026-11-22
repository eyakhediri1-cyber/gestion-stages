package tn.gestionstages.backend.dto;

public record StudentProfileRequest(
    String university,
    String specialization,
    String studyLevel,
    String phone,
    String cvUrl,
    String linkedinUrl,
    String githubUrl
) {}
