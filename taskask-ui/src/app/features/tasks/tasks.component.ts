import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { TaskService } from '../../core/services/task.service';
import { EmployeeService } from '../../core/services/employee.service';
import { AuthService } from '../../core/services/auth.service';
import {
  PRIORITY_META,
  STATUS_META,
  TaskCommentResponse,
  TaskPriority,
  TaskResponse,
  TaskStatus
} from '../../core/models/task.model';
import { EmployeeResponse } from '../../core/models/employee.model';
import { PageResponse } from '../../core/models/pagination.model';
import { StatusBadgeComponent } from '../../shared/ui/status-badge.component';
import { EmptyStateComponent } from '../../shared/ui/empty-state.component';
import { SpinnerComponent } from '../../shared/ui/spinner.component';
import { AppRole } from '../../core/constants/app.constants';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, StatusBadgeComponent, EmptyStateComponent, SpinnerComponent],
  templateUrl: './tasks.component.html'
})
export class TasksComponent implements OnInit {
  protected readonly TaskStatus = TaskStatus;
  protected readonly TaskPriority = TaskPriority;
  protected readonly statuses = Object.values(TaskStatus);
  protected readonly priorities = Object.values(TaskPriority);
  protected readonly statusMeta = STATUS_META;
  protected readonly priorityMeta = PRIORITY_META;

  protected readonly tasks = signal<TaskResponse[]>([]);
  protected readonly selectedTask = signal<TaskResponse | null>(null);
  protected readonly comments = signal<TaskCommentResponse[]>([]);
  protected readonly employees = signal<EmployeeResponse[]>([]);
  protected readonly loadingList = signal(false);
  protected readonly loadingAction = signal(false);
  protected readonly showCreate = signal(false);

  protected readonly activeAssignees = computed(() => this.selectedTask()?.assigneeNames?.join(', ') ?? '—');

  private readonly fb = inject(FormBuilder);
  private readonly taskService = inject(TaskService);
  private readonly employeeService = inject(EmployeeService);
  private readonly authService = inject(AuthService);

  readonly filterForm = this.fb.group({
    status: this.fb.control<TaskStatus | null>(null),
    assigneeId: this.fb.control<number | null>(null)
  });

  readonly taskForm = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.maxLength(255)]],
    description: ['', [Validators.maxLength(1000)]],
    priority: [TaskPriority.MEDIUM, Validators.required],
    startDate: [''],
    dueDate: [''],
    assigneeIds: this.fb.nonNullable.control<number[]>([], { validators: Validators.required })
  });

  readonly commentForm = this.fb.nonNullable.group({
    comment: ['', [Validators.required, Validators.maxLength(2000)]]
  });

  ngOnInit(): void {
    this.loadEmployees();
    this.loadTasks();
  }

  loadEmployees(): void {
    this.employeeService.list().subscribe({
      next: employees => this.employees.set(employees),
      error: () => this.employees.set([])
    });
  }

  loadTasks(): void {
    this.loadingList.set(true);
    const { status, assigneeId } = this.filterForm.getRawValue();
    this.taskService
      .list({ status: status ?? undefined, assigneeId: assigneeId ?? undefined, size: 50 })
      .subscribe({
        next: (page: PageResponse<TaskResponse>) => {
          this.tasks.set(page.content);
          this.loadingList.set(false);
        },
        error: () => this.loadingList.set(false)
      });
  }

  selectTask(task: TaskResponse): void {
    this.selectedTask.set(task);
    this.taskService.listComments(task.id).subscribe(comments => this.comments.set(comments));
  }

  createTask(): void {
    if (this.taskForm.invalid) {
      this.taskForm.markAllAsTouched();
      return;
    }
    this.loadingAction.set(true);
    const payload = this.taskForm.getRawValue();
    const formatted = {
      ...payload,
      startDate: payload.startDate || null,
      dueDate: payload.dueDate || null,
      assigneeIds: (payload.assigneeIds as unknown as Array<number | string>).map(value => Number(value))
    };
    this.taskService.create(formatted).subscribe({
      next: task => {
        this.loadingAction.set(false);
        this.showCreate.set(false);
        this.taskForm.reset({ priority: TaskPriority.MEDIUM, assigneeIds: [] as number[] });
        this.tasks.update(list => [task, ...list]);
      },
      error: () => this.loadingAction.set(false)
    });
  }

  updateStatus(task: TaskResponse, status: TaskStatus): void {
    const nextStatus = status;
    if (task.status === nextStatus) {
      return;
    }
    this.taskService.updateStatus(task.id, nextStatus).subscribe(updated => {
      this.tasks.update(items => items.map(item => (item.id === updated.id ? updated : item)));
      if (this.selectedTask()?.id === updated.id) {
        this.selectedTask.set(updated);
      }
    });
  }

  addComment(): void {
    if (!this.selectedTask()) {
      return;
    }
    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }
    const taskId = this.selectedTask()!.id;
    this.taskService.addComment(taskId, this.commentForm.getRawValue()).subscribe(comment => {
      this.comments.update(list => [...list, comment]);
      this.commentForm.reset({ comment: '' });
    });
  }

  onStatusChange(task: TaskResponse, event: Event): void {
    const select = event.target as HTMLSelectElement | null;
    if (!select) {
      return;
    }
    this.updateStatus(task, select.value as TaskStatus);
  }

  protected canManageTasks(): boolean {
    return this.authService.hasRole([AppRole.ADMIN, AppRole.MANAGER]);
  }
}
