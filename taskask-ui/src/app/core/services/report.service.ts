import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { EmployeeReportResponse } from '../models/report.model';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly baseUrl = `${environment.apiUrl}/reports`;

  constructor(private readonly http: HttpClient) {}

  employeeReport(employeeId: number): Observable<EmployeeReportResponse> {
    return this.http.get<EmployeeReportResponse>(`${this.baseUrl}/employee/${employeeId}`);
  }
}
