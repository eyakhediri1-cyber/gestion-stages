
package tn.gestionstages.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import tn.gestionstages.backend.dto.StudentProfileRequest;
import tn.gestionstages.backend.dto.StudentProfileResponse;
import tn.gestionstages.backend.entity.AppUser;
import tn.gestionstages.backend.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    public StudentProfileResponse getMyProfile(
            @AuthenticationPrincipal AppUser user) {

        return studentService.getProfile(user);
    }

    @PutMapping("/me")
    public StudentProfileResponse updateMyProfile(
            @AuthenticationPrincipal AppUser user,
            @RequestBody StudentProfileRequest request) {

        return studentService.updateProfile(user, request);
    }
}
