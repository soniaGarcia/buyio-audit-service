package com.buyio.audit.listener;

import com.buyio.audit.domain.AuditLog;
import com.buyio.audit.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventListener {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"UserEvents", "CatalogEvents", "OrderEvents"}, groupId = "${spring.kafka.consumer.group-id}")
    public void handleEvents(ConsumerRecord<String, String> record) {
        log.info("Evento auditado recibido desde tópico [{}]: {}", record.topic(), record.value());
        try {
            String payloadStr = record.value();
            JsonNode jsonNode = objectMapper.readTree(payloadStr);

            String eventType = jsonNode.has("eventType") ? jsonNode.get("eventType").asText() : "UNKNOWN_EVENT";
            String entityId = extractEntityId(jsonNode);

            AuditLog auditLog = AuditLog.builder()
                    .sourceService(mapTopicToService(record.topic()))
                    .eventType(eventType)
                    .entityId(entityId)
                    .payload(payloadStr)
                    .build();

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error al procesar y persistir el registro de auditoría", e);
        }
    }

    private String mapTopicToService(String topic) {
        return switch (topic) {
            case "UserEvents" -> "buyio-auth-service";
            case "CatalogEvents" -> "buyio-catalog-service";
            case "OrderEvents" -> "buyio-order-service";
            default -> "unknown-service";
        };
    }

    private String extractEntityId(JsonNode node) {
        if (node.has("userId")) return node.get("userId").asText();
        if (node.has("productId")) return node.get("productId").asText();
        if (node.has("orderId")) return node.get("orderId").asText();
        if (node.has("id")) return node.get("id").asText();
        return null;
    }
}