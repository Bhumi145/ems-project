package com.example.taskask.dto;

import java.util.List;

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
public class EmployeeReportResponse {
    private long totalTasks;
    private long completedTasks;
    private double completionRate;
    private long overdueTasks;
    private List<MonthlySummaryResponse> monthlySummary;
}
