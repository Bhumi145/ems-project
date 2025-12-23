package com.example.taskask.service;

import com.example.taskask.entity.TaskHistory;
import com.example.taskask.repository.TaskHistoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskHistoryService {
    private final TaskHistoryRepository taskHistoryRepository;
    public TaskHistoryService(TaskHistoryRepository taskHistoryRepository) { this.taskHistoryRepository = taskHistoryRepository; }
    public List<TaskHistory> getHistoryByTaskId(Long taskId) { return taskHistoryRepository.findByTaskId(taskId); }
    public TaskHistory save(TaskHistory history) { return taskHistoryRepository.save(history); }
}
