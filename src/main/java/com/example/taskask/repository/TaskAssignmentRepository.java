package com.example.taskask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskask.entity.TaskAssignment;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
    List<TaskAssignment> findByTaskId(Long taskId);
    List<TaskAssignment> findByAssigneeId(Long assigneeId);

    boolean existsByTaskIdAndAssigneeUserEmail(Long taskId, String email);

    List<TaskAssignment> findByAssigneeManagerId(Long managerId);
}
