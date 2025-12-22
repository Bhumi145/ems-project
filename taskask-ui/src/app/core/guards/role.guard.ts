import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';
import { AppRole } from '../constants/app.constants';

export const roleGuard: CanMatchFn = (route: Route, segments: UrlSegment[]) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const requiredRoles = (route.data?.['roles'] as AppRole[]) ?? [];

  if (requiredRoles.length === 0 || authService.hasRole(requiredRoles)) {
    return true;
  }

  router.navigate(['/']);
  return false;
};
