package com.example.taskask.repository;

import com.example.taskask.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    List<TaskHistory> findByTaskId(Long taskId);
}
