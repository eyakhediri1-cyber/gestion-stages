package tn.gestionstages.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.gestionstages.backend.dto.StudentProfileRequest;
import tn.gestionstages.backend.dto.StudentProfileResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.entity.Student;
import tn.gestionstages.backend.enums.Role;
import tn.gestionstages.backend.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private Student getStudent(AppUser user) {

        if (user.getRole() != Role.STUDENT) {
            throw new AccessDeniedException(
                "Accès réservé aux étudiants"
            );
        }

        return studentRepository.findByUserId(user.getId())
            .orElseGet(() -> {
                Student student = new Student();
                student.setUser(user);
                return studentRepository.save(student);
            });
    }

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfile(AppUser user) {

        Student student = getStudent(user);

        return toResponse(student);
    }

    @Transactional
    public StudentProfileResponse updateProfile(
        AppUser user,
        StudentProfileRequest request
    ) {

        Student student = getStudent(user);

        student.setUniversity(request.university());
        student.setSpecialization(request.specialization());
        student.setStudyLevel(request.studyLevel());
        student.setPhone(request.phone());
        student.setCvUrl(request.cvUrl());
        student.setLinkedinUrl(request.linkedinUrl());
        student.setGithubUrl(request.githubUrl());

        return toResponse(studentRepository.save(student));
    }

    private StudentProfileResponse toResponse(Student student) {

        AppUser user = student.getUser();

        return new StudentProfileResponse(
            student.getId(),
            user.getFullName(),
            user.getEmail(),
            student.getUniversity(),
            student.getSpecialization(),
            student.getStudyLevel(),
            student.getPhone(),
            student.getCvUrl(),
            student.getLinkedinUrl(),
            student.getGithubUrl()
        );
    }
}
