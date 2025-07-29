package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateProducerService {
    private static final String TOPIC = "live-update";
    private final KafkaTemplate<String, LiveUpdateRequestDto> liveUpdateKafkaTemplate;

    public void produceLiveUpdate(LiveUpdateRequestDto liveUpdateRequestDto) {
        String driverId = liveUpdateRequestDto.getDriverId().toString();
        CompletableFuture<SendResult<String, LiveUpdateRequestDto>> future =
                liveUpdateKafkaTemplate.send(TOPIC, driverId, liveUpdateRequestDto);

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
