package com.example.taskask.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskask.dto.CreateUserRequest;
import com.example.taskask.dto.UserResponse;
import com.example.taskask.entity.User;
import com.example.taskask.enums.Role;
import com.example.taskask.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User saved = createInternalUser(request.getEmail(), request.getFullName(), request.getPassword(), request.getRole());
        return toResponse(saved);
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toResponse(user);
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.from(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.from(user);
    }

    public User createInternalUser(String email, String fullName, String rawPassword, Role role) {
        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(rawPassword))
            .fullName(fullName != null ? fullName : email)
            .role(role != null ? role : Role.EMPLOYEE)
            .active(true)
            .build();
        return userRepository.save(user);
    }
}
