import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pie-chart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col items-center">
      <svg width="120" height="120" viewBox="0 0 40 40">
        <circle r="16" cx="20" cy="20" fill="#222" />
        <circle
          *ngIf="pending > 0"
          r="16" cx="20" cy="20"
          [attr.stroke]="'#fbbf24'"
          [attr.stroke-width]="6"
          [attr.stroke-dasharray]="pendingArc + ', 100'"
          [attr.stroke-dashoffset]="0"
          fill="none"
          stroke-linecap="round"
        />
        <circle
          *ngIf="inProgress > 0"
          r="16" cx="20" cy="20"
          [attr.stroke]="'#3b82f6'"
          [attr.stroke-width]="6"
          [attr.stroke-dasharray]="inProgressArc + ', 100'"
          [attr.stroke-dashoffset]="-pendingArc"
          fill="none"
          stroke-linecap="round"
        />
        <circle
          *ngIf="completed > 0"
          r="16" cx="20" cy="20"
          [attr.stroke]="'#22c55e'"
          [attr.stroke-width]="6"
          [attr.stroke-dasharray]="completedArc + ', 100'"
          [attr.stroke-dashoffset]="-(pendingArc + inProgressArc)"
          fill="none"
          stroke-linecap="round"
        />
      </svg>
      <div class="flex flex-col gap-1 mt-2 text-xs">
        <span><span class="inline-block w-3 h-3 rounded-full mr-1" style="background:#fbbf24"></span>Pending: {{ pending }}</span>
        <span><span class="inline-block w-3 h-3 rounded-full mr-1" style="background:#3b82f6"></span>In Progress: {{ inProgress }}</span>
        <span><span class="inline-block w-3 h-3 rounded-full mr-1" style="background:#22c55e"></span>Completed: {{ completed }}</span>
      </div>
    </div>
  `
})
export class PieChartComponent {
  @Input() pending = 0;
  @Input() inProgress = 0;
  @Input() completed = 0;

  get total() {
    return this.pending + this.inProgress + this.completed;
  }
  get pendingArc() {
    return this.total ? (this.pending / this.total) * 100 : 0;
  }
  get inProgressArc() {
    return this.total ? (this.inProgress / this.total) * 100 : 0;
  }
  get completedArc() {
    return this.total ? (this.completed / this.total) * 100 : 0;
  }
}
