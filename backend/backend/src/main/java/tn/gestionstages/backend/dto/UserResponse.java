package tn.gestionstages.backend.dto;

import tn.gestionstages.backend.enums.Role;

public record UserResponse(
    Long id,
    String fullName,
    String email,
    Role role
) {}
