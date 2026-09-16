package com.buyio.audit.controller;

import com.buyio.audit.dto.AuditLogDto;
import com.buyio.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLogDto>> getAllLogs(@RequestParam(required = false) String service) {
        if (service != null && !service.isBlank()) {
            return ResponseEntity.ok(auditLogService.getLogsByService(service));
        }
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDto> getLogById(@PathVariable UUID id) {
        return ResponseEntity.ok(auditLogService.getLogById(id));
    }
}