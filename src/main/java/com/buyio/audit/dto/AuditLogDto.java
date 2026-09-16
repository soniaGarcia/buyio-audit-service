package com.buyio.audit.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class AuditLogDto {
    private UUID id;
    private String sourceService;
    private String eventType;
    private String entityId;
    private String payload;
    private OffsetDateTime createdAt;
}