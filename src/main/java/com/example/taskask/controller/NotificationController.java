package com.example.taskask.controller;

import com.example.taskask.entity.Notification;
import com.example.taskask.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) { this.notificationService = notificationService; }
    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }
    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) { return notificationService.save(notification); }
}
