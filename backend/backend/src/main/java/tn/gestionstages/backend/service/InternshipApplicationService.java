package tn.gestionstages.backend.service;

import tn.gestionstages.backend.dto.InternshipApplicationRequest;
import tn.gestionstages.backend.entity.Company;
import tn.gestionstages.backend.entity.InternshipApplication;
import tn.gestionstages.backend.entity.InternshipOffer;
import tn.gestionstages.backend.entity.Student;
import tn.gestionstages.backend.enums.ApplicationStatus;
import tn.gestionstages.backend.repository.CompanyRepository;
import tn.gestionstages.backend.repository.InternshipApplicationRepository;
import tn.gestionstages.backend.repository.InternshipOfferRepository;
import tn.gestionstages.backend.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InternshipApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final InternshipOfferRepository offerRepository;
    private final CompanyRepository companyRepository;

    public InternshipApplicationService(
            InternshipApplicationRepository applicationRepository,
            StudentRepository studentRepository,
            InternshipOfferRepository offerRepository,
            CompanyRepository companyRepository) {

        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.offerRepository = offerRepository;
        this.companyRepository = companyRepository;
    }

    // Retrieve the student profile of the authenticated user
    private Student getStudent(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Student profile not found"));
    }

    // Retrieve the company profile of the authenticated user
    private Company getCompany(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Company profile not found"));
    }

    // Apply to a published internship offer
    public InternshipApplication apply(
            Long userId,
            Long offerId,
            InternshipApplicationRequest request) {

        Student student = getStudent(userId);

        InternshipOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Internship offer not found"));

        if (!offer.isPublished()) {
            throw new IllegalStateException("This offer is not published");
        }

        if (offer.getApplicationDeadline() != null
                && offer.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Application deadline has passed");
        }

        if (applicationRepository.existsByStudentIdAndOfferId(
                student.getId(), offerId)) {
            throw new IllegalStateException(
                    "You have already applied to this offer");
        }

        InternshipApplication application = new InternshipApplication();

        application.setStudent(student);
        application.setOffer(offer);
        application.setCoverLetter(request.coverLetter());

        String cvUrl = request.cvUrl();

        if (cvUrl == null || cvUrl.isBlank()) {
            cvUrl = student.getCvUrl();
        }

        application.setCvUrl(cvUrl);
        application.setStatus(ApplicationStatus.PENDING);

        return applicationRepository.save(application);
    }

    // Get all applications submitted by the authenticated student
    @Transactional(readOnly = true)
    public List<InternshipApplication> getStudentApplications(Long userId) {

        Student student = getStudent(userId);

        return applicationRepository.findByStudentId(student.getId());
    }

    // Withdraw a student's own application
    public InternshipApplication withdraw(
            Long applicationId,
            Long userId) {

        Student student = getStudent(userId);

        InternshipApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Application not found"));

        if (!application.getStudent().getId().equals(student.getId())) {
            throw new AccessDeniedException(
                    "You cannot withdraw another student's application");
        }

        if (application.getStatus() == ApplicationStatus.ACCEPTED
                || application.getStatus() == ApplicationStatus.REJECTED
                || application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new IllegalStateException(
                    "This application can no longer be withdrawn");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);

        return applicationRepository.save(application);
    }

    // Get applications received for an offer owned by the company
    @Transactional(readOnly = true)
    public List<InternshipApplication> getOfferApplications(
            Long offerId,
            Long userId) {

        Company company = getCompany(userId);

        InternshipOffer offer = offerRepository.findById(offerId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Internship offer not found"));

        if (!offer.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException(
                    "You cannot view applications for this offer");
        }

        return applicationRepository.findByOfferId(offerId);
    }

    // Update the status of an application received by the company
    public InternshipApplication updateStatus(
            Long applicationId,
            Long userId,
            ApplicationStatus newStatus) {

        Company company = getCompany(userId);

        InternshipApplication application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Application not found"));

        if (!application.getOffer().getCompany().getId()
                .equals(company.getId())) {
            throw new AccessDeniedException(
                    "You cannot manage this application");
        }

        if (newStatus != ApplicationStatus.REVIEWING
                && newStatus != ApplicationStatus.ACCEPTED
                && newStatus != ApplicationStatus.REJECTED) {
            throw new IllegalArgumentException(
                    "Invalid status for company update");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new IllegalStateException(
                    "A withdrawn application cannot be updated");
        }

        application.setStatus(newStatus);

        return applicationRepository.save(application);
    }
}
