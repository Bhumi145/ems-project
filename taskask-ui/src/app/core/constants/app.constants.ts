// Centralized role and route constants for maintainability and scalability
// Use these everywhere instead of hardcoding strings

export enum AppRole {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  EMPLOYEE = 'EMPLOYEE',
}

export const ROLE_LABELS: Record<AppRole, string> = {
  [AppRole.ADMIN]: 'Admin',
  [AppRole.MANAGER]: 'Manager',
  [AppRole.EMPLOYEE]: 'Employee',
};

export const ROUTES = {
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  ADMIN_DASHBOARD: '/admin',
  MANAGER_DASHBOARD: '/manager',
  EMPLOYEE_DASHBOARD: '/employee',
  TASKS: '/tasks',
  USERS: '/users',
  EMPLOYEES: '/employees',
  REPORTS: '/reports',
  EMPLOYEE_PERFORMANCE_GRAPH: '/employee/performance-graph',
};
