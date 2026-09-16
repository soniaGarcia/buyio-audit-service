package com.buyio.audit.repository;

import com.buyio.audit.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findBySourceService(String sourceService);
    List<AuditLog> findByEventType(String eventType);
}