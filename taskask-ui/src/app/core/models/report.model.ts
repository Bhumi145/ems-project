export interface MonthlySummaryResponse {
  year: number;
  month: number;
  total: number;
  completed: number;
  completionRate: number;
}

export interface EmployeeReportResponse {
  totalTasks: number;
  completedTasks: number;
  completionRate: number;
  overdueTasks: number;
  monthlySummary: MonthlySummaryResponse[];
}
