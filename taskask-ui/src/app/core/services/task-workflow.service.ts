import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TaskWorkflowService {
  private apiUrl = `${environment.apiUrl}/tasks`;

  constructor(private http: HttpClient) {}

  submitForReview(taskId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${taskId}/submit-for-review`, {});
  }

  approve(taskId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${taskId}/approve`, {});
  }

  reject(taskId: number, comment?: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${taskId}/reject`, comment || '', {
      headers: { 'Content-Type': 'text/plain' }
    });
  }

  complete(taskId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${taskId}/complete`, {});
  }

  rework(taskId: number, comment?: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${taskId}/rework`, comment || '', {
      headers: { 'Content-Type': 'text/plain' }
    });
  }
}
