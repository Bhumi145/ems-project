import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col gap-3 rounded-3xl border border-white/5 bg-white/5 p-5 transition hover:border-white/20">
      <p class="text-xs uppercase tracking-[0.3rem] text-white/50">{{ label }}</p>
      <p class="text-3xl font-semibold">{{ value }}</p>
      @if (hint) {
        <p class="text-sm text-white/60">{{ hint }}</p>
      }
    </div>
  `
})
export class StatCardComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) value!: string | number;
  @Input() hint?: string;
}
