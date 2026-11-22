package tn.gestionstages.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import tn.gestionstages.backend.dto.AuthResponse;
import tn.gestionstages.backend.dto.LoginRequest;
import tn.gestionstages.backend.dto.RegisterRequest;
import tn.gestionstages.backend.dto.UserResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.entity.Company;
import tn.gestionstages.backend.entity.Student;
import tn.gestionstages.backend.enums.Role;
import tn.gestionstages.backend.repository.CompanyRepository;
import tn.gestionstages.backend.repository.StudentRepository;
import tn.gestionstages.backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {

        String normalizedEmail =
                request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette adresse email est déjà utilisée"
            );
        }

        if (request.role() != Role.STUDENT
                && request.role() != Role.COMPANY) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rôle non autorisé pour l'inscription"
            );
        }

        if (request.role() == Role.COMPANY
                && (request.companyName() == null
                || request.companyName().isBlank())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nom de l'entreprise est obligatoire"
            );
        }

        AppUser user = new AppUser();

        user.setFullName(request.fullName().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(request.role());
        user.setEnabled(true);

        AppUser savedUser = userRepository.save(user);

        if (request.role() == Role.STUDENT) {

            Student student = new Student();
            student.setUser(savedUser);

            studentRepository.save(student);

        } else if (request.role() == Role.COMPANY) {

            Company company = new Company();
            company.setUser(savedUser);
            company.setCompanyName(
                    request.companyName().trim()
            );

            companyRepository.save(company);
        }

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {

        String normalizedEmail =
                request.email().trim().toLowerCase();

        AppUser user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadCredentialsException(
                        "Email ou mot de passe incorrect"
                ));

        if (!user.isEnabled()
                || !passwordEncoder.matches(
                        request.password(),
                        user.getPasswordHash())) {

            throw new BadCredentialsException(
                    "Email ou mot de passe incorrect"
            );
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}