import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { AppRole } from './core/constants/app.constants';
import { MainLayoutComponent } from './layouts/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { TasksComponent } from './features/tasks/tasks.component';
import { ReportsComponent } from './features/reports/reports.component';
import { EmployeesComponent } from './features/employees/employees.component';
import { LoginComponent } from './features/auth/login.component';
import { RegisterComponent } from './features/auth/register.component';

export const routes: Routes = [
	{
		path: '',
		component: MainLayoutComponent,
		canActivate: [authGuard],
		children: [
			{ path: '', component: DashboardComponent },
			{ path: 'tasks', component: TasksComponent },
			{ path: 'reports', component: ReportsComponent },
			{
				path: 'employees',
				component: EmployeesComponent,
				canMatch: [roleGuard],
				data: { roles: [AppRole.ADMIN, AppRole.MANAGER] }
			},
			{
				path: 'hr',
				loadChildren: () => import('./features/hr/hr.module').then(m => m.HrModule),
				canMatch: [roleGuard],
				data: { roles: [AppRole.HR] }
			},
			{
				path: 'team-lead',
				loadChildren: () => import('./features/team-lead/team-lead.module').then(m => m.TeamLeadModule),
				canMatch: [roleGuard],
				data: { roles: [AppRole.TEAM_LEAD] }
			}
		]
	},
	{
		path: 'auth',
		component: AuthLayoutComponent,
		children: [
			{ path: 'login', component: LoginComponent },
			{ path: 'register', component: RegisterComponent },
			{ path: '', pathMatch: 'full', redirectTo: 'login' }
		]
	},
	{ path: '**', redirectTo: '' }
];
