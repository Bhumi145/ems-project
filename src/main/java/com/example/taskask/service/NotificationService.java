package com.example.taskask.service;


import com.example.taskask.entity.Notification;
import com.example.taskask.entity.Task;
import com.example.taskask.repository.NotificationRepository;
import com.example.taskask.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getNotificationsByUserId(Long userId) { return notificationRepository.findByUserId(userId); }
    public Notification save(Notification notification) { return notificationRepository.save(notification); }

    // Escalation levels: 1=Team Lead, 2=Manager, 3=Admin
    public void sendSlaEscalationNotification(Task task, int escalationLevel) {
        String role;
        switch (escalationLevel) {
            case 1: role = "TEAM_LEAD"; break;
            case 2: role = "MANAGER"; break;
            case 3: role = "ADMIN"; break;
            default: return;
        }
        // Find all users with the escalation role
        List<Long> userIds = userRepository.findAll().stream()
            .filter(u -> u.getRole() != null && u.getRole().name().equals(role))
            .map(u -> u.getId())
            .toList();
        for (Long userId : userIds) {
            Notification n = new Notification();
            n.setUserId(userId);
            n.setType("SLA_ESCALATION");
            n.setMessage("Task #" + task.getId() + " breached SLA and escalated to " + role.replace('_', ' '));
            n.setIsRead(false);
            n.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(n);
        }
    }
}
