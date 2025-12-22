import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { EmployeeService } from '../../core/services/employee.service';
import { EmployeeResponse, CreateEmployeeRequest } from '../../core/models/employee.model';
import { AppRole, ROLE_LABELS } from '../../core/constants/app.constants';
import { EmptyStateComponent } from '../../shared/ui/empty-state.component';

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, EmptyStateComponent],
  templateUrl: './employees.component.html'
})
export class EmployeesComponent implements OnInit {
  protected readonly employees = signal<EmployeeResponse[]>([]);
  protected readonly roles = Object.values(AppRole);
  protected readonly roleLabels = ROLE_LABELS;
  protected readonly loading = signal(false);

  private readonly fb = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);

  readonly form = this.fb.nonNullable.group({
    fullName: ['', Validators.required],
    department: [''],
    title: [''],
    managerId: [null as number | null],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: [AppRole.EMPLOYEE, Validators.required]
  });

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading.set(true);
    this.employeeService.list().subscribe({
      next: employees => {
        this.employees.set(employees);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  create(): void {
    if (this.form.invalid) {
      console.warn('[Employees] Form invalid', this.form.getRawValue(), this.form.errors);
      this.form.markAllAsTouched();
      return;
    }
    const raw = this.form.getRawValue();
    console.log('[Employees] Submit clicked', raw);
    const managerIdRaw = raw.managerId as number | string | null | undefined;
    let normalizedManagerId: number | null = null;
    if (typeof managerIdRaw === 'number' && !Number.isNaN(managerIdRaw)) {
      normalizedManagerId = managerIdRaw;
    } else if (typeof managerIdRaw === 'string' && managerIdRaw.trim().length > 0) {
      const parsed = Number(managerIdRaw);
      normalizedManagerId = Number.isNaN(parsed) ? null : parsed;
    }

    const payload: CreateEmployeeRequest = {
      ...raw,
      role: (raw.role ?? AppRole.EMPLOYEE) as AppRole,
      managerId: normalizedManagerId
    };

    this.employeeService.createEmployee(payload).subscribe({
      next: employee => {
        console.log('[Employees] Created teammate', employee);
        this.loadEmployees();
        this.form.reset({
          fullName: '',
          department: '',
          title: '',
          managerId: null,
          email: '',
          password: '',
          role: AppRole.EMPLOYEE
        });
      },
      error: error => {
        console.error('[Employees] Failed to create teammate', error);
      }
    });
  }

  deactivate(id: number): void {
    this.employeeService.delete(id).subscribe(() => {
      this.employees.set(this.employees().filter(employee => employee.id !== id));
    });
  }
}
