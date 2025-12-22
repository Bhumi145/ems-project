package com.example.taskask.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskask.dto.CreateTaskRequest;
import com.example.taskask.dto.TaskCommentRequest;
import com.example.taskask.dto.TaskCommentResponse;
import com.example.taskask.dto.TaskDashboardResponse;
import com.example.taskask.dto.TaskResponse;
import com.example.taskask.dto.UpdateTaskStatusRequest;
import com.example.taskask.enums.TaskStatus;
import com.example.taskask.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public Page<TaskResponse> listTasks(@RequestParam(required = false) TaskStatus status,
                                        @RequestParam(required = false) Long assigneeId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        Principal principal) {
        return taskService.getTasks(status, assigneeId, page, size, principal.getName());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public TaskResponse updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody UpdateTaskStatusRequest request,
                                     Principal principal) {
        return taskService.updateStatus(id, request.getStatus(), principal.getName());
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public TaskDashboardResponse dashboard(Principal principal) {
        return taskService.getDashboard(principal.getName());
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public TaskCommentResponse addComment(@PathVariable Long id,
                                          @Valid @RequestBody TaskCommentRequest request,
                                          Principal principal) {
        return taskService.addComment(id, request, principal.getName());
    }

    @GetMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public List<TaskCommentResponse> listComments(@PathVariable Long id, Principal principal) {
        return taskService.getComments(id, principal.getName());
    }
}
