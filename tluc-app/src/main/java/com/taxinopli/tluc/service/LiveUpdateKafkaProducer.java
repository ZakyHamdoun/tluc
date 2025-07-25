package com.taxinopli.tluc.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taxinopli.tluc.dto.LiveUpdateRequestDto;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateKafkaProducer {

    private final String TOPIC = "taxi-live-update";

    private final KafkaTemplate<String, LiveUpdateRequestDto> kafkaTemplate;

    public void sendToKafka(LiveUpdateRequestDto liveUpdateDto) {
        String driverId = liveUpdateDto.getDriverUuid().toString();
        
        CompletableFuture<SendResult<String, LiveUpdateRequestDto>> future = 
            kafkaTemplate.send(TOPIC, driverId, liveUpdateDto);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Taxi location update sent successfully to Kafka topic: {} for driver: {}", 
                        TOPIC, driverId);
            } else {
                log.error("Failed to send taxi location update to Kafka topic: {} for driver: {}", 
                        TOPIC, driverId, ex);
            }
        });
    }
}   
