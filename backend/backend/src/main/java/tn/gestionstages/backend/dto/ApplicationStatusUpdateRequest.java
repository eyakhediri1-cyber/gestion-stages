package tn.gestionstages.backend.dto;

import tn.gestionstages.backend.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusUpdateRequest(
    @NotNull ApplicationStatus status
) {}
