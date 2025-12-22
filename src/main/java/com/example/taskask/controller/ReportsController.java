package com.example.taskask.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskask.dto.EmployeeReportResponse;
import com.example.taskask.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final TaskService taskService;

    @GetMapping("/employee/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public EmployeeReportResponse employeeReport(@PathVariable Long id, Principal principal) {
        return taskService.getEmployeeReport(id, principal.getName());
    }
}
