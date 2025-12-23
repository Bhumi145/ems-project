package com.example.taskask.enums;

public enum TaskStatus {
    DRAFT,
    REVIEW,
    APPROVED,
    COMPLETED,
    REJECTED,
    REWORK,
    // legacy statuses for backward compatibility
    OPEN,
    IN_PROGRESS,
    BLOCKED
}
