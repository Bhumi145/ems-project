import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="grid min-h-[80vh] gap-8 rounded-[40px] bg-surface/70 p-6 shadow-card glass-panel lg:grid-cols-2 lg:p-10">
      <div class="hidden flex-col justify-between rounded-3xl border border-white/5 bg-gradient-to-br from-base-700/60 via-surface to-base-900/80 p-8 text-white lg:flex">
        <div>
          <p class="text-sm uppercase tracking-[0.3rem] text-white/50">ETPMS 360°</p>
          <h1 class="mt-4 text-4xl font-semibold leading-tight">
            Track performance, orchestrate execution, and celebrate wins across every role.
          </h1>
          <p class="mt-4 text-base text-white/70">
            Real-time dashboards, automated task governance, and narrative reporting designed for Admins, Managers, and Employees in one collaborative workspace.
          </p>
        </div>
        <div class="space-y-3">
          <p class="text-xs uppercase tracking-widest text-white/40">TRUSTED FLOW</p>
          <div class="grid gap-3 text-sm">
            <div class="rounded-2xl border border-white/5 bg-white/5 p-4">
              <p class="text-xs text-white/60">Compliance</p>
              <p class="text-lg font-semibold">Audit ready logbooks</p>
            </div>
            <div class="rounded-2xl border border-white/5 bg-white/5 p-4">
              <p class="text-xs text-white/60">People Ops</p>
              <p class="text-lg font-semibold">Role aware automation</p>
            </div>
          </div>
        </div>
      </div>
      <div class="flex items-center justify-center">
        <div class="w-full max-w-md rounded-3xl border border-white/5 bg-white/5 p-6 shadow-card">
          <router-outlet />
        </div>
      </div>
    </section>
  `
})
export class AuthLayoutComponent {}
