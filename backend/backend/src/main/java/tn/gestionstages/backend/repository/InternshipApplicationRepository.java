package tn.gestionstages.backend.repository;

import tn.gestionstages.backend.entity.InternshipApplication;
import tn.gestionstages.backend.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipApplicationRepository
        extends JpaRepository<InternshipApplication, Long> {

    List<InternshipApplication> findByStudentId(Long studentId);

    List<InternshipApplication> findByOfferId(Long offerId);

    List<InternshipApplication> findByStatus(ApplicationStatus status);

    boolean existsByStudentIdAndOfferId(Long studentId, Long offerId);

    boolean existsByOfferId(Long offerId);
}
