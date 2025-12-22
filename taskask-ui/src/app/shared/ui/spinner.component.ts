import { Component } from '@angular/core';

@Component({
  selector: 'app-spinner',
  standalone: true,
  template: `
    <span class="inline-flex items-center gap-2 text-white/70">
      <span class="h-3 w-3 animate-ping rounded-full bg-accent"></span>
      Loading
    </span>
  `
})
export class SpinnerComponent {}
