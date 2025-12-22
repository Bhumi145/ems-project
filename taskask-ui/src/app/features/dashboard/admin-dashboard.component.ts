import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Total Employees</p>
        <h2 class="text-3xl font-bold">{{ totalEmployees }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Total Managers</p>
        <h2 class="text-3xl font-bold">{{ totalManagers }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Total Tasks</p>
        <h2 class="text-3xl font-bold">{{ totalTasks }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Completed Tasks</p>
        <h2 class="text-3xl font-bold">{{ completedTasks }}</h2>
      </div>
    </div>
  `
})
export class AdminDashboardComponent {
  // These would be loaded from a service in a real app
  totalEmployees = 0;
  totalManagers = 0;
  totalTasks = 0;
  completedTasks = 0;
}
