package com.example.taskask.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.taskask.enums.TaskPriority;
import com.example.taskask.enums.TaskStatus;

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
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String assigneeUsername;
    private List<String> assigneeNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
