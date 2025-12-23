import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HrDashboardComponent } from './hr-dashboard.component';
import { roleGuard } from '../../core/guards/role.guard';
import { AppRole } from '../../core/constants/app.constants';

const routes: Routes = [
  {
    path: '',
    component: HrDashboardComponent,
    canMatch: [roleGuard],
    data: { roles: [AppRole.HR] }
  }
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes), HrDashboardComponent]
})
export class HrModule {}
