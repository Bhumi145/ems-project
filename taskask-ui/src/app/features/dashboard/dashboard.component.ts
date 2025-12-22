import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskService } from '../../core/services/task.service';
import { TaskDashboardResponse, TaskResponse, TaskStatus } from '../../core/models/task.model';
import { PageResponse } from '../../core/models/pagination.model';
import { StatCardComponent } from '../../shared/ui/stat-card.component';
import { StatusBadgeComponent } from '../../shared/ui/status-badge.component';
import { SpinnerComponent } from '../../shared/ui/spinner.component';
import { EmptyStateComponent } from '../../shared/ui/empty-state.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, StatCardComponent, StatusBadgeComponent, SpinnerComponent, EmptyStateComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  protected readonly snapshot = signal<TaskDashboardResponse | null>(null);
  protected readonly spotlight = signal<TaskResponse[]>([]);
  protected readonly loading = signal(true);

  constructor(private readonly taskService: TaskService) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.loading.set(true);
    this.taskService.dashboard().subscribe(snapshot => this.snapshot.set(snapshot));
    this.taskService.list({ size: 5, status: TaskStatus.IN_PROGRESS }).subscribe({
      next: (page: PageResponse<TaskResponse>) => {
        this.spotlight.set(page.content);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}
