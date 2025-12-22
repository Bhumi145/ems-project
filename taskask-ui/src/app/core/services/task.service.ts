import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { PageResponse } from '../models/pagination.model';
import {
  CreateTaskRequest,
  TaskCommentRequest,
  TaskCommentResponse,
  TaskDashboardResponse,
  TaskResponse,
  TaskStatus,
  UpdateTaskStatusRequest
} from '../models/task.model';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private readonly baseUrl = `${environment.apiUrl}/tasks`;

  constructor(private readonly http: HttpClient) {}

  dashboard(): Observable<TaskDashboardResponse> {
    return this.http.get<TaskDashboardResponse>(`${this.baseUrl}/dashboard`);
  }

  list(params: {
    status?: TaskStatus;
    assigneeId?: number;
    page?: number;
    size?: number;
  } = {}): Observable<PageResponse<TaskResponse>> {
    let httpParams = new HttpParams();
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        httpParams = httpParams.set(key, value);
      }
    });
    return this.http.get<PageResponse<TaskResponse>>(this.baseUrl, { params: httpParams });
  }

  create(payload: CreateTaskRequest): Observable<TaskResponse> {
    return this.http.post<TaskResponse>(this.baseUrl, payload);
  }

  updateStatus(id: number, status: TaskStatus): Observable<TaskResponse> {
    const body: UpdateTaskStatusRequest = { status };
    return this.http.patch<TaskResponse>(`${this.baseUrl}/${id}/status`, body);
  }

  addComment(id: number, payload: TaskCommentRequest): Observable<TaskCommentResponse> {
    return this.http.post<TaskCommentResponse>(`${this.baseUrl}/${id}/comments`, payload);
  }

  listComments(id: number): Observable<TaskCommentResponse[]> {
    return this.http.get<TaskCommentResponse[]>(`${this.baseUrl}/${id}/comments`);
  }
}
