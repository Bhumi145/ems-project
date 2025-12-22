import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { AppRole, ROLE_LABELS } from '../../core/constants/app.constants';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="flex min-h-screen items-center justify-center bg-gradient-to-br from-base-900 to-base-800 p-4">
      <div class="w-full max-w-md rounded-3xl bg-black/40 p-8 shadow-xl">
        <form class="space-y-6" [formGroup]="form" (ngSubmit)="register()">
          <div class="space-y-2 text-center">
            <p class="text-xs uppercase tracking-[0.4rem] text-white/50">Register</p>
            <h2 class="text-2xl font-semibold text-white">Create workspace identity</h2>
            <p class="text-sm text-white/60">Provision a role to unlock the correct cockpit.</p>
          </div>
          <div class="space-y-2">
            <label class="block text-sm text-white/70">Full name</label>
            <input
              type="text"
              formControlName="fullName"
              class="w-full rounded-2xl border border-white/10 bg-black/40 px-4 py-3 text-white focus:border-accent focus:outline-none"
              placeholder="Jamie Lannister"
            />
            <ng-container *ngIf="form.get('fullName')?.touched && form.get('fullName')?.invalid">
              <p class="text-xs text-danger mt-1" *ngIf="form.get('fullName')?.errors?.['required']">Full name is required.</p>
              <p class="text-xs text-danger mt-1" *ngIf="form.get('fullName')?.errors?.['minlength']">Full name must be at least 3 characters.</p>
            </ng-container>
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
            <label class="block text-sm text-white/70">Role</label>
            <select formControlName="role" class="w-full rounded-2xl border border-white/10 bg-black/40 px-4 py-3 text-white focus:border-accent focus:outline-none">
              <ng-container *ngFor="let role of roles">
                <option [value]="role">{{ roleLabels[role] }}</option>
              </ng-container>
            </select>
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
              <p class="text-xs text-danger mt-1" *ngIf="form.get('password')?.errors?.['minlength']">Password must be at least 6 characters.</p>
            </ng-container>
          </div>
          <ng-container *ngIf="error()">
            <p class="rounded-2xl border border-danger/20 bg-danger/10 px-4 py-2 text-sm text-danger text-center">{{ error() }}</p>
          </ng-container>
          <button type="submit" class="w-full rounded-2xl bg-accent/90 px-4 py-3 font-semibold text-black hover:bg-accent/80 flex items-center justify-center" [disabled]="form.invalid || loading()">
            <ng-container *ngIf="loading(); else createText">
              <span class="loader mr-2"></span> Provisioning...
            </ng-container>
            <ng-template #createText>Create identity</ng-template>
          </button>
          <p class="text-center text-xs text-white/50">
            Already have credentials? <a routerLink="/auth/login" class="font-semibold text-accent">Sign in</a>
          </p>
        </form>
      </div>
    </div>
  `
})
export class RegisterComponent {
  // Use centralized AppRole enum for roles
  protected readonly roles = Object.values(AppRole);
  protected readonly roleLabels = ROLE_LABELS;
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly form = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    role: [AppRole.EMPLOYEE, Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  register(): void {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.authService.register(this.form.getRawValue()).subscribe({
      next: () => {
        this.loading.set(false);
        this.authService.logout();
        this.router.navigate(['/auth/login']);
      },
      error: err => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Registration failed');
      }
    });
  }
}
