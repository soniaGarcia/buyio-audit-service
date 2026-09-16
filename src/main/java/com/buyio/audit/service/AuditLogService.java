package com.buyio.audit.service;

import com.buyio.audit.domain.AuditLog;
import com.buyio.audit.dto.AuditLogDto;
import com.buyio.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(readOnly = true)
    public List<AuditLogDto> getAllLogs() {
        return auditLogRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public AuditLogDto getLogById(UUID id) {
        AuditLog log = auditLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de auditoría no encontrado"));
        return mapToDto(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto> getLogsByService(String service) {
        return auditLogRepository.findBySourceService(service).stream().map(this::mapToDto).toList();
    }

    private AuditLogDto mapToDto(AuditLog log) {
        return AuditLogDto.builder()
                .id(log.getId())
                .sourceService(log.getSourceService())
                .eventType(log.getEventType())
                .entityId(log.getEntityId())
                .payload(log.getPayload())
                .createdAt(log.getCreatedAt())
                .build();
    }
}