package tn.gestionstages.backend.dto;

import jakarta.validation.constraints.Size;

public record InternshipApplicationRequest(

        @Size(max = 5000)
        String coverLetter,

        String cvUrl

) {}
