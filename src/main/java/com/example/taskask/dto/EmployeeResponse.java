package com.example.taskask.dto;

import com.example.taskask.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String department;
    private String title;
    private Role role;
    private Long managerId;
    private boolean active;
}
