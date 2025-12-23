package com.example.taskask.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Column(nullable = false)
    private String fromStatus;
    @Column(nullable = false)
    private String toStatus;
    private Long changedBy;
    private LocalDateTime changedAt;
    private String comment;
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public Long getChangedBy() { return changedBy; }
    public void setChangedBy(Long changedBy) { this.changedBy = changedBy; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
