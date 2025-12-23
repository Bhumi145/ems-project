import { CommonModule, DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { NotificationService } from '../../core/services/notification.service';

@Component({
  selector: 'app-notification-panel',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './notification-panel.component.html',
  styleUrls: ['./notification-panel.component.scss']
})
export class NotificationPanelComponent implements OnInit {
  notifications: any[] = [];
  loading = false;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.fetchNotifications();
  }

  fetchNotifications() {
    this.loading = true;
    this.notificationService.getMyNotifications().subscribe({
      next: (data) => {
        this.notifications = data;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }
}
