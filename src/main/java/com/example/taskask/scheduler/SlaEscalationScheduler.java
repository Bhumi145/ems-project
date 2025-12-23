package com.example.taskask.scheduler;

import com.example.taskask.entity.Task;
import com.example.taskask.enums.TaskStatus;
import com.example.taskask.repository.TaskRepository;
import com.example.taskask.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlaEscalationScheduler {
    @Autowired
    private final TaskRepository taskRepository;

    private final NotificationService notificationService;

    // Escalation levels: 0=Employee, 1=Team Lead, 2=Manager, 3=Admin
    @Scheduled(cron = "0 */5 * * * *") // every 5 minutes
    @Transactional
    public void checkSlaBreaches() {
        List<Task> overdue = taskRepository.findByStatusNotAndDueDateBefore(TaskStatus.COMPLETED, LocalDate.now());
        for (Task task : overdue) {
            int prevLevel = task.getEscalationLevel();
            if (prevLevel < 3) {
                task.setEscalationLevel(prevLevel + 1);
                // Optionally, set a flag or status for SLA breach
                // task.setSlaBreached(true);
                taskRepository.save(task);
                // Send notification to next escalation role
                notificationService.sendSlaEscalationNotification(task, prevLevel + 1);
                log.info("Task {} escalated to level {}", task.getId(), prevLevel + 1);
            }
        }
    }
}
