package tn.gestionstages.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InternshipOfferRequest(

        @NotBlank
        @Size(max = 255)
        String title,

        @NotBlank
        String description,

        String domain,

        @NotBlank
        String location,

        String internshipType,

        String duration,

        LocalDate startDate,

        LocalDate applicationDeadline,

        String requiredSkills

) {}
