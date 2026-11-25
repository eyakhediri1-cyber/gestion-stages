package tn.gestionstages.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tn.gestionstages.backend.dto.AuthResponse;
import tn.gestionstages.backend.dto.LoginRequest;
import tn.gestionstages.backend.dto.RegisterRequest;
import tn.gestionstages.backend.dto.UserResponse;
import tn.gestionstages.backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
                System.out.println("========== LOGIN CONTROLLER REACHED ==========");

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
}
}
