import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col items-center justify-center gap-2 rounded-3xl border border-dashed border-white/10 bg-white/5 p-8 text-center text-white/60">
      <p class="text-3xl">{{ emoji }}</p>
      <p class="text-base font-semibold text-white">{{ title }}</p>
      @if (description) {
        <p class="text-sm text-white/60">{{ description }}</p>
      }
      <ng-content />
    </div>
  `
})
export class EmptyStateComponent {
  @Input() title = 'Nothing to see yet';
  @Input() description?: string;
  @Input() emoji = '✨';
}
