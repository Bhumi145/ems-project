import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';


import { AuthService } from '../../core/services/auth.service';
import { AppRole, ROUTES } from '../../core/constants/app.constants';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="flex min-h-screen items-center justify-center bg-gradient-to-br from-base-900 to-base-800 p-4">
      <div class="w-full max-w-md rounded-3xl bg-black/40 p-8 shadow-xl">
        <form class="space-y-6" [formGroup]="form" (ngSubmit)="signin()">
          <div class="space-y-2 text-center">
            <p class="text-xs uppercase tracking-[0.4rem] text-white/50">Login</p>
            <h2 class="text-2xl font-semibold text-white">Welcome back</h2>
            <p class="text-sm text-white/60">Authenticate to access role-specific dashboards.</p>
          </div>
          <div class="space-y-2">
            <label class="block text-sm text-white/70">Work email</label>
            <input
              type="email"
              formControlName="email"
              class="w-full rounded-2xl border border-white/10 bg-black/40 px-4 py-3 text-white focus:border-accent focus:outline-none"
              placeholder="team@company.com"
            />
            <ng-container *ngIf="form.get('email')?.touched && form.get('email')?.invalid">
              <p class="text-xs text-danger mt-1" *ngIf="form.get('email')?.errors?.['required']">Email is required.</p>
              <p class="text-xs text-danger mt-1" *ngIf="form.get('email')?.errors?.['email']">Enter a valid email.</p>
            </ng-container>
          </div>
          <div class="space-y-2">
            <label class="block text-sm text-white/70">Password</label>
            <input
              type="password"
              formControlName="password"
              class="w-full rounded-2xl border border-white/10 bg-black/40 px-4 py-3 text-white focus:border-accent focus:outline-none"
              placeholder="••••••••"
            />
            <ng-container *ngIf="form.get('password')?.touched && form.get('password')?.invalid">
              <p class="text-xs text-danger mt-1" *ngIf="form.get('password')?.errors?.['required']">Password is required.</p>
              <p class="text-xs text-danger mt-1" *ngIf="form.get('password')?.errors?.['minlength']">Password must be at least 4 characters.</p>
            </ng-container>
          </div>
          <ng-container *ngIf="error()">
            <p class="rounded-2xl border border-danger/20 bg-danger/10 px-4 py-2 text-sm text-danger text-center">{{ error() }}</p>
          </ng-container>
          <button type="submit" class="w-full rounded-2xl bg-accent/90 px-4 py-3 font-semibold text-black hover:bg-accent/80 flex items-center justify-center" [disabled]="form.invalid || loading()">
            <ng-container *ngIf="loading(); else enterText">
              <span class="loader mr-2"></span> Processing...
            </ng-container>
            <ng-template #enterText>Enter workspace</ng-template>
          </button>
          <p class="text-center text-xs text-white/50">
            Need an account? <a routerLink="/auth/register" class="font-semibold text-accent">Create one</a>
          </p>
        </form>
      </div>
    </div>
  `
})
export class LoginComponent {
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  // Improved: role-based redirect after login, clear error handling
  signin(): void {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.authService.login(this.form.getRawValue()).subscribe({
      next: (res) => {
        this.loading.set(false);
        // Role-based redirect
        if (res?.role === AppRole.ADMIN) {
          this.router.navigate([ROUTES.ADMIN_DASHBOARD]);
        } else if (res?.role === AppRole.MANAGER) {
          this.router.navigate([ROUTES.MANAGER_DASHBOARD]);
        } else {
          this.router.navigate([ROUTES.EMPLOYEE_DASHBOARD]);
        }
      },
      error: err => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Unable to authenticate.');
      }
    });
  }
}
