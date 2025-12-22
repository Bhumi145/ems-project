import { CommonModule } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { AppRole, ROLE_LABELS } from '../core/constants/app.constants';
import { AuthService } from '../core/services/auth.service';

interface NavigationItem {
  label: string;
  icon: string;
  link: string;
  roles?: AppRole[];
}

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly user = this.authService.currentUser;
  protected readonly role = computed(() => this.authService.role);
  protected readonly roleLabel = computed(() => {
    const role = this.authService.role;
    return role ? ROLE_LABELS[role] : '—';
  });

  protected readonly menu: NavigationItem[] = [
    { label: 'Mission Control', icon: '🚀', link: '/' },
    { label: 'Tasks Board', icon: '🗂️', link: '/tasks' },
    { label: 'Performance Pulse', icon: '📈', link: '/reports' },
    { label: 'People Ops', icon: '👥', link: '/employees', roles: [AppRole.ADMIN, AppRole.MANAGER] }
  ];

  protected canRender(item: NavigationItem): boolean {
    if (!item.roles?.length) {
      return true;
    }
    return this.authService.hasRole(item.roles);
  }

  protected logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
