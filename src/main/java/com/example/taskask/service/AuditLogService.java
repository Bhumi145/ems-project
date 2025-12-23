package com.example.taskask.service;

import com.example.taskask.entity.AuditLog;
import com.example.taskask.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    public AuditLogService(AuditLogRepository auditLogRepository) { this.auditLogRepository = auditLogRepository; }
    public List<AuditLog> getLogsByUserId(Long userId) { return auditLogRepository.findByUserId(userId); }
    public AuditLog save(AuditLog log) { return auditLogRepository.save(log); }

    public void logAudit(String action, String entity, Long entityId, String details, Long userId) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setUserId(userId);
        log.setCreatedAt(LocalDateTime.now());
        save(log);
    }
}
