package com.example.taskask.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskask.dto.CreateEmployeeRequest;
import com.example.taskask.dto.EmployeeResponse;
import com.example.taskask.entity.EmployeeProfile;
import com.example.taskask.entity.User;
import com.example.taskask.repository.EmployeeProfileRepository;
import com.example.taskask.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeProfileRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public EmployeeResponse create(CreateEmployeeRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = userService.createInternalUser(request.getEmail(), request.getFullName(), request.getPassword(), request.getRole());

        EmployeeProfile manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
        }

        EmployeeProfile profile = EmployeeProfile.builder()
                .user(user)
                .fullName(request.getFullName())
                .department(request.getDepartment())
                .title(request.getTitle())
                .manager(manager)
                .build();

        EmployeeProfile saved = employeeRepository.save(profile);
        return toResponse(saved);
    }

    public EmployeeResponse get(Long id) {
        return employeeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    public List<EmployeeResponse> list() {
        return employeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    private EmployeeResponse toResponse(EmployeeProfile p) {
        Long managerId = p.getManager() != null ? p.getManager().getId() : null;
        return EmployeeResponse.builder()
                .id(p.getId())
                .userId(p.getUser().getId())
                .fullName(p.getFullName())
                .department(p.getDepartment())
                .title(p.getTitle())
                .role(p.getUser().getRole())
                .managerId(managerId)
                .active(p.getUser().isActive())
                .build();
    }
}
