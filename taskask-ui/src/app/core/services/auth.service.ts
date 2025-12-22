import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

import { AuthResponse, AuthSession, LoginRequest, RegisterRequest } from '../models/auth.model';
import { AppRole } from '../constants/app.constants';
import { environment } from '../../../environments/environment';

const STORAGE_KEY = 'taskask/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly session = signal<AuthSession | null>(this.restoreSession());

  readonly currentUser = computed(() => this.session());
  readonly isAuthenticated = computed(() => !!this.session()?.token);

  constructor(private readonly http: HttpClient) {}

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, payload).pipe(
      tap(response => this.persistSession(response))
    );
  }

  register(payload: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, payload).pipe(
      tap(response => this.persistSession(response))
    );
  }

  logout(): void {
    this.session.set(null);
    localStorage.removeItem(STORAGE_KEY);
  }

  get token(): string | null {
    return this.session()?.token ?? null;
  }

  get role(): AppRole | null {
    return this.session()?.role ?? null;
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated();
  }

  hasRole(roles: AppRole[]): boolean {
    const current = this.session();
    return !!current && roles.includes(current.role);
  }

  private persistSession(value: AuthSession): void {
    this.session.set(value);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
  }

  private restoreSession(): AuthSession | null {
    if (typeof window === 'undefined') {
      return null;
    }
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as AuthSession;
    } catch (error) {
      console.warn('Unable to parse auth session payload', error);
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
