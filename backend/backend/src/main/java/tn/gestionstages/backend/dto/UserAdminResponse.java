
package tn.gestionstages.backend.dto;

import tn.gestionstages.backend.enums.Role;
import java.time.LocalDateTime;

public record UserAdminResponse(
    Long id,
    String fullName,
    String email,
    Role role,
    boolean enabled,
    LocalDateTime createdAt
) {}
