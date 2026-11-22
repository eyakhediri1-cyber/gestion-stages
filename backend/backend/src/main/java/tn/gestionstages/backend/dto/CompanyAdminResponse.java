package tn.gestionstages.backend.dto;

public record CompanyAdminResponse(
    Long id,
    Long userId,
    String companyName,
    String industry,
    String description,
    String website,
    String address,
    String phone,
    String logoUrl,
    boolean verified
) {}
