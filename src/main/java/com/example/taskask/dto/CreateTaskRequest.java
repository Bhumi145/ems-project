package com.example.taskask.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.taskask.enums.TaskPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateTaskRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private TaskPriority priority;

    private LocalDate startDate;

    private LocalDate dueDate;

    @NotEmpty
    private List<Long> assigneeIds;

    public boolean isDateRangeValid() {
        if (startDate == null || dueDate == null) {
            return true;
        }
        return !dueDate.isBefore(startDate);
    }
}
