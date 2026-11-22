package tn.gestionstages.backend.entity;

import tn.gestionstages.backend.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "internship_applications",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"student_id", "offer_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private InternshipOffer offer;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String cvUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
