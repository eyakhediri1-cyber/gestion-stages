
package tn.gestionstages.backend.service;

import tn.gestionstages.backend.enums.ApplicationStatus;

import tn.gestionstages.backend.entity.InternshipApplication;

import tn.gestionstages.backend.dto.ApplicationAdminResponse;

import tn.gestionstages.backend.entity.InternshipOffer;

import tn.gestionstages.backend.dto.OfferAdminResponse;

import tn.gestionstages.backend.dto.CompanyAdminResponse;

import tn.gestionstages.backend.entity.Company;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import tn.gestionstages.backend.dto.AdminDashboardResponse;
import tn.gestionstages.backend.dto.UserAdminResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.repository.CompanyRepository;
import tn.gestionstages.backend.repository.InternshipApplicationRepository;
import tn.gestionstages.backend.repository.InternshipOfferRepository;
import tn.gestionstages.backend.repository.StudentRepository;
import tn.gestionstages.backend.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final InternshipOfferRepository offerRepository;
    private final InternshipApplicationRepository applicationRepository;

    public AdminService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            InternshipOfferRepository offerRepository,
            InternshipApplicationRepository applicationRepository) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.offerRepository = offerRepository;
        this.applicationRepository = applicationRepository;
    }

    private UserAdminResponse toResponse(AppUser user) {
        return new UserAdminResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole(),
            user.isEnabled(),
            user.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<UserAdminResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public UserAdminResponse getUserById(Long id) {
        return toResponse(findUser(id));
    }

    @Transactional
    public UserAdminResponse disableUser(Long id) {
        AppUser user = findUser(id);
        user.setEnabled(false);
        return toResponse(user);
    }

    @Transactional
    public UserAdminResponse enableUser(Long id) {
        AppUser user = findUser(id);
        user.setEnabled(true);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard() {

        long totalUsers = userRepository.count();
        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();
        long totalOffers = offerRepository.count();
        long totalApplications = applicationRepository.count();

        long disabledUsers = userRepository.findAll()
            .stream()
            .filter(user -> !user.isEnabled())
            .count();

        List<UserAdminResponse> recentUsers =
            userRepository.findAll()
                .stream()
                .sorted(
                    Comparator.comparing(
                        AppUser::getCreatedAt,
                        Comparator.nullsLast(
                            Comparator.reverseOrder()
                        )
                    )
                )
                .limit(5)
                .map(this::toResponse)
                .toList();

        return new AdminDashboardResponse(
            totalUsers,
            totalStudents,
            totalCompanies,
            totalOffers,
            totalApplications,
            disabledUsers,
            recentUsers
        );
    }

    private AppUser findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Utilisateur introuvable"
            ));
    }

    private CompanyAdminResponse toCompanyResponse(Company company) {
        return new CompanyAdminResponse(
            company.getId(),
            company.getUser().getId(),
            company.getCompanyName(),
            company.getIndustry(),
            company.getDescription(),
            company.getWebsite(),
            company.getAddress(),
            company.getPhone(),
            company.getLogoUrl(),
            company.isVerified()
        );
    }

    @Transactional(readOnly = true)
    public List<CompanyAdminResponse> getAllCompanies() {
        return companyRepository.findAll()
            .stream()
            .map(this::toCompanyResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public CompanyAdminResponse getCompanyById(Long id) {
        return toCompanyResponse(findCompany(id));
    }

    @Transactional
    public CompanyAdminResponse verifyCompany(Long id) {
        Company company = findCompany(id);
        company.setVerified(true);
        return toCompanyResponse(company);
    }

    @Transactional
    public CompanyAdminResponse unverifyCompany(Long id) {
        Company company = findCompany(id);
        company.setVerified(false);
        return toCompanyResponse(company);
    }

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Entreprise introuvable"
            ));
    }


    private OfferAdminResponse toOfferResponse(InternshipOffer offer) {
        return new OfferAdminResponse(
            offer.getId(),
            offer.getCompany().getId(),
            offer.getCompany().getCompanyName(),
            offer.getTitle(),
            offer.getDescription(),
            offer.getDomain(),
            offer.getLocation(),
            offer.getInternshipType(),
            offer.getDuration(),
            offer.getStartDate(),
            offer.getApplicationDeadline(),
            offer.getRequiredSkills(),
            offer.isPublished(),
            offer.getCreatedAt(),
            offer.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<OfferAdminResponse> getAllOffers() {
        return offerRepository.findAll()
            .stream()
            .map(this::toOfferResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public OfferAdminResponse getOfferById(Long id) {
        return toOfferResponse(findOffer(id));
    }

    @Transactional
    public OfferAdminResponse publishOffer(Long id) {
        InternshipOffer offer = findOffer(id);
        offer.setPublished(true);
        return toOfferResponse(offer);
    }

    @Transactional
    public OfferAdminResponse unpublishOffer(Long id) {
        InternshipOffer offer = findOffer(id);
        offer.setPublished(false);
        return toOfferResponse(offer);
    }

    @Transactional
    public void deleteOffer(Long id) {
        InternshipOffer offer = findOffer(id);

        if (applicationRepository.existsByOfferId(id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Impossible de supprimer une offre ayant des candidatures"
            );
        }

        offerRepository.delete(offer);
    }

    private InternshipOffer findOffer(Long id) {
        return offerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Offre introuvable"
            ));
    }


    private ApplicationAdminResponse toApplicationResponse(
            InternshipApplication application) {

        return new ApplicationAdminResponse(
            application.getId(),
            application.getStudent().getId(),
            application.getStudent().getUser().getFullName(),
            application.getStudent().getUser().getEmail(),
            application.getOffer().getId(),
            application.getOffer().getTitle(),
            application.getOffer().getCompany().getCompanyName(),
            application.getCoverLetter(),
            application.getCvUrl(),
            application.getStatus(),
            application.getAppliedAt(),
            application.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ApplicationAdminResponse> getAllApplications() {
        return applicationRepository.findAll()
            .stream()
            .map(this::toApplicationResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ApplicationAdminResponse getApplicationById(Long id) {
        return toApplicationResponse(findApplication(id));
    }

    @Transactional
    public ApplicationAdminResponse updateApplicationStatus(
            Long id, ApplicationStatus status) {

        if (status == null || status == ApplicationStatus.WITHDRAWN) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Statut administratif invalide"
            );
        }

        InternshipApplication application = findApplication(id);
        application.setStatus(status);

        return toApplicationResponse(application);
    }

    private InternshipApplication findApplication(Long id) {
        return applicationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Candidature introuvable"
            ));
    }

}
