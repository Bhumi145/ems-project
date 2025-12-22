import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

import { PRIORITY_META, STATUS_META, TaskPriority, TaskStatus } from '../../core/models/task.model';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="rounded-full px-3 py-1 text-xs font-semibold" [ngClass]="badgeClass">
      {{ label }}
    </span>
  `
})
export class StatusBadgeComponent {
  @Input() status?: TaskStatus;
  @Input() priority?: TaskPriority;

  get badgeClass(): string {
    if (this.status) {
      return STATUS_META[this.status].classes;
    }
    if (this.priority) {
      return PRIORITY_META[this.priority].classes;
    }
    return 'bg-white/10 text-white';
  }

  get label(): string {
    if (this.status) {
      return STATUS_META[this.status].label;
    }
    if (this.priority) {
      return PRIORITY_META[this.priority].label;
    }
    return 'N/A';
  }
}
