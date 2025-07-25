package com.taxinopli.tluc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealtimeMonitoringService {

    private final DataSource dataSource;
    private final KafkaTemplate<String, ?> kafkaTemplate;

    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("timestamp", Instant.now());
        
        try {
            boolean dbHealthy = checkDatabaseHealth();
            health.put("database", Map.of(
                "status", dbHealthy ? "UP" : "DOWN",
                "description", dbHealthy ? "Connected to TimescaleDB" : "Cannot connect to TimescaleDB"
            ));
            
            boolean kafkaHealthy = checkKafkaHealth();
            health.put("kafka", Map.of(
                "status", kafkaHealthy ? "UP" : "DOWN",
                "description", kafkaHealthy ? "Connected to Kafka cluster" : "Cannot connect to Kafka cluster"
            ));

            boolean systemHealthy = dbHealthy && kafkaHealthy;
            health.put("overall", Map.of(
                "status", systemHealthy ? "UP" : "DOWN",
                "realtime-pipeline", systemHealthy ? "Fully operational" : "Degraded performance"
            ));
            
        } catch (Exception e) {
            log.error("Health check failed", e);
            health.put("overall", Map.of(
                "status", "DOWN",
                "error", e.getMessage()
            ));
        }
        
        return health;
    }

    private boolean checkDatabaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5); // 5 seconds timeout
        } catch (Exception e) {
            log.warn("Database health check failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean checkKafkaHealth() {
        try {
            // Simple way to check if Kafka is accessible
            kafkaTemplate.getProducerFactory().createProducer().close();
            return true;
        } catch (Exception e) {
            log.warn("Kafka health check failed: {}", e.getMessage());
            return false;
        }
    }
}
