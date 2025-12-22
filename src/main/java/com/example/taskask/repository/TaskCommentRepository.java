package com.example.taskask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskask.entity.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskId(Long taskId);
}
