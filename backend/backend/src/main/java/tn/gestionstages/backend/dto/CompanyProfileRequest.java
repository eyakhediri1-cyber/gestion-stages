package tn.gestionstages.backend.dto;

public record CompanyProfileRequest(
    String companyName,
    String industry,
    String description,
    String website,
    String address,
    String phone,
    String logoUrl
) {}
