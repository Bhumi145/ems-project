package com.example.taskask.controller;

import com.example.taskask.entity.TaskHistory;
import com.example.taskask.service.TaskHistoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/history")
public class TaskHistoryController {
    private final TaskHistoryService taskHistoryService;
    public TaskHistoryController(TaskHistoryService taskHistoryService) { this.taskHistoryService = taskHistoryService; }
    @GetMapping
    public List<TaskHistory> getTaskHistory(@PathVariable Long taskId) {
        return taskHistoryService.getHistoryByTaskId(taskId);
    }
}
