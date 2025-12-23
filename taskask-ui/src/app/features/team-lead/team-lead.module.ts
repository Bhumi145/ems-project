import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { TeamLeadDashboardComponent } from './team-lead-dashboard.component';
import { roleGuard } from '../../core/guards/role.guard';
import { AppRole } from '../../core/constants/app.constants';

const routes: Routes = [
  {
    path: '',
    component: TeamLeadDashboardComponent,
    canMatch: [roleGuard],
    data: { roles: [AppRole.TEAM_LEAD] }
  }
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes), TeamLeadDashboardComponent]
})
export class TeamLeadModule {}
