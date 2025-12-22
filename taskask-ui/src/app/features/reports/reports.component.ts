import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { EmployeeService } from '../../core/services/employee.service';
import { ReportService } from '../../core/services/report.service';
import { EmployeeReportResponse } from '../../core/models/report.model';
import { EmployeeResponse } from '../../core/models/employee.model';
import { StatCardComponent } from '../../shared/ui/stat-card.component';
import { EmptyStateComponent } from '../../shared/ui/empty-state.component';
import { SpinnerComponent } from '../../shared/ui/spinner.component';
import { PerformanceGraphComponent } from '../../core/constants/performance-graph.component';
import { PieChartComponent } from './pie-chart.component';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, StatCardComponent, EmptyStateComponent, SpinnerComponent, PerformanceGraphComponent, PieChartComponent],
  templateUrl: './reports.component.html'
})
export class ReportsComponent implements OnInit {
  protected readonly employees = signal<EmployeeResponse[]>([]);
  protected readonly report = signal<EmployeeReportResponse | null>(null);
  protected readonly loading = signal(false);

  private readonly fb = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);
  private readonly reportService = inject(ReportService);

  readonly form = this.fb.nonNullable.group({
    employeeId: [null as number | null]
  });

  ngOnInit(): void {
    this.employeeService.list().subscribe(list => {
      this.employees.set(list);
      const first = list[0]?.id ?? null;
      this.form.patchValue({ employeeId: first });
      if (first) {
        this.loadReport(first);
      }
    });
  }

  submit(): void {
    const id = this.form.value.employeeId;
    if (id) {
      this.loadReport(id);
    }
  }

  private loadReport(employeeId: number): void {
    this.loading.set(true);
    this.reportService.employeeReport(employeeId).subscribe({
      next: payload => {
        this.report.set(payload);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}
