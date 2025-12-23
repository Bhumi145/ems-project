package com.example.taskask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskask.dto.AuthResponse;
import com.example.taskask.dto.CreateUserRequest;
import com.example.taskask.dto.LoginRequest;
import com.example.taskask.entity.User;
import com.example.taskask.repository.UserRepository;
import com.example.taskask.service.AuditLogService;
import com.example.taskask.service.AuthService;
import com.example.taskask.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody CreateUserRequest request) {
        // Only allow Admin/Manager registration if no users exist yet
        long userCount = userRepository.count();
        if (userCount > 0 && (request.getRole() == null || request.getRole().name().equals("ADMIN") || request.getRole().name().equals("MANAGER"))) {
            throw new IllegalArgumentException("Admin/Manager registration is only allowed as the first user.");
        }
        User user = authService.register(request);
        auditLogService.logAudit("REGISTER_USER", "USER", user.getId(), "User registered with email: " + user.getEmail(), user.getId());
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .id(user.getId())
                .build();
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        User user = userRepository.findByEmail(response.getEmail()).orElse(null);
        if (user != null) {
            auditLogService.logAudit("LOGIN", "USER", user.getId(), "User logged in", user.getId());
        }
        return AuthResponse.builder()
                .token(response.getToken())
                .email(response.getEmail())
                .role(response.getRole())
                .id(user != null ? user.getId() : null)
                .build();
    }
}
