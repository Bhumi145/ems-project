import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">My Tasks</p>
        <h2 class="text-3xl font-bold">{{ myTasks }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Completed</p>
        <h2 class="text-3xl font-bold">{{ completed }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">In Progress</p>
        <h2 class="text-3xl font-bold">{{ inProgress }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Overdue</p>
        <h2 class="text-3xl font-bold">{{ overdue }}</h2>
      </div>
    </div>
  `
})
export class EmployeeDashboardComponent {
  // These would be loaded from a service in a real app
  myTasks = 0;
  completed = 0;
  inProgress = 0;
  overdue = 0;
}
