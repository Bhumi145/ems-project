package com.example.taskask.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.taskask.entity.Task;
import com.example.taskask.enums.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

    interface MonthlySummaryProjection {
        Integer getYear();
        Integer getMonth();
        Long getTotal();
        Long getCompleted();
    }

    List<Task> findByStatus(TaskStatus status);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    long countByStatus(TaskStatus status);

    @Query("select distinct t from Task t join TaskAssignment ta on ta.task = t where ta.assignee.id = :assigneeId and (:status is null or t.status = :status)")
    Page<Task> findByAssigneeIdAndStatus(@Param("assigneeId") Long assigneeId, @Param("status") TaskStatus status, Pageable pageable);

    @Query("select distinct t from Task t join TaskAssignment ta on ta.task = t where (ta.assignee.manager.id = :managerId or ta.assignee.id = :managerId) and (:status is null or t.status = :status)")
    Page<Task> findByManagerIdAndStatus(@Param("managerId") Long managerId, @Param("status") TaskStatus status, Pageable pageable);

    @Query("select count(distinct t.id) from Task t join TaskAssignment ta on ta.task = t where ta.assignee.id = :assigneeId and (:status is null or t.status = :status)")
    long countByAssigneeIdAndStatus(@Param("assigneeId") Long assigneeId, @Param("status") TaskStatus status);

    @Query("select count(distinct t.id) from Task t join TaskAssignment ta on ta.task = t where (ta.assignee.manager.id = :managerId or ta.assignee.id = :managerId) and (:status is null or t.status = :status)")
    long countByManagerIdAndStatus(@Param("managerId") Long managerId, @Param("status") TaskStatus status);

    @Query("select count(distinct t.id) from Task t join TaskAssignment ta on ta.task = t where ta.assignee.id = :assigneeId")
    long countByAssigneeId(@Param("assigneeId") Long assigneeId);

    @Query("select count(distinct t.id) from Task t join TaskAssignment ta on ta.task = t where ta.assignee.id = :assigneeId and t.dueDate is not null and t.dueDate < :today and t.status <> 'COMPLETED'")
    long countOverdueByAssignee(@Param("assigneeId") Long assigneeId, @Param("today") LocalDate today);

    @Query("select year(coalesce(t.dueDate, t.startDate, t.createdAt)) as year, month(coalesce(t.dueDate, t.startDate, t.createdAt)) as month, count(distinct t.id) as total, sum(case when t.status = 'COMPLETED' then 1 else 0 end) as completed from Task t join TaskAssignment ta on ta.task = t where ta.assignee.id = :assigneeId group by year(coalesce(t.dueDate, t.startDate, t.createdAt)), month(coalesce(t.dueDate, t.startDate, t.createdAt)) order by year(coalesce(t.dueDate, t.startDate, t.createdAt)), month(coalesce(t.dueDate, t.startDate, t.createdAt))")
    List<MonthlySummaryProjection> monthlySummaryByAssignee(@Param("assigneeId") Long assigneeId);
}
