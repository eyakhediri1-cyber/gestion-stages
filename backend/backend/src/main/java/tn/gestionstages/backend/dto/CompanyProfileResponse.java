package tn.gestionstages.backend.dto;

public record CompanyProfileResponse(
    Long id,
    String companyName,
    String industry,
    String description,
    String website,
    String address,
    String phone,
    String logoUrl,
    boolean verified
) {}
