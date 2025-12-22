import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Team Members</p>
        <h2 class="text-3xl font-bold">{{ teamMembers }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Assigned Tasks</p>
        <h2 class="text-3xl font-bold">{{ assignedTasks }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Completed Tasks</p>
        <h2 class="text-3xl font-bold">{{ completedTasks }}</h2>
      </div>
      <div class="rounded-2xl bg-base-800 p-6 text-white shadow-card">
        <p class="text-xs uppercase text-white/50">Overdue Tasks</p>
        <h2 class="text-3xl font-bold">{{ overdueTasks }}</h2>
      </div>
    </div>
  `
})
export class ManagerDashboardComponent {
  // These would be loaded from a service in a real app
  teamMembers = 0;
  assignedTasks = 0;
  completedTasks = 0;
  overdueTasks = 0;
}
