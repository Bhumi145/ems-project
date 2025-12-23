package com.example.taskask.controller;

import com.example.taskask.entity.AuditLog;
import com.example.taskask.service.AuditLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    private final AuditLogService auditLogService;
    public AuditLogController(AuditLogService auditLogService) { this.auditLogService = auditLogService; }
    @GetMapping("/user/{userId}")
    public List<AuditLog> getLogsByUser(@PathVariable Long userId) {
        return auditLogService.getLogsByUserId(userId);
    }
    @PostMapping
    public AuditLog createLog(@RequestBody AuditLog log) { return auditLogService.save(log); }
}
