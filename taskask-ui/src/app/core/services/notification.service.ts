import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  getMyNotifications(): Observable<any[]> {
    const userId = this.auth.currentUser()?.id;
    return this.http.get<any[]>(`${environment.apiUrl}/notifications/user/${userId}`);
  }
}
