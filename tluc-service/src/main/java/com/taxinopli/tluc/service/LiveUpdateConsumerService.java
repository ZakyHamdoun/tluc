package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.entity.LiveDriverEntity;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.mapper.LiveUpdateMapper;
import com.taxinopli.tluc.model.LiveUpdateModel;
import com.taxinopli.tluc.repository.LiveDriverRepository;
import com.taxinopli.tluc.repository.LiveUpdateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unified Real-time Location Service
 * This service handles both Kafka consumption AND business logic for live updates
 * Single responsibility: Process real-time taxi location data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateConsumerService {
    @Autowired
    private final LiveUpdateRepository liveUpdateRepository;

    @Autowired
    private final LiveDriverRepository liveDriverRepository;

    /**
     * Core business logic: Process location update
     * Follows layered architecture - business layer works with DTOs and Models only
     * No entity instances created in business layer!
     */
    @KafkaListener(topics = "live-update", groupId = "live-update-group")
    @Transactional
    public void consumeLiveUpdate(LiveUpdateRequestDto liveUpdateRequestDto) {
        log.debug("Processing taxi location update for driver: {}", liveUpdateRequestDto.getDriverId());
        LiveUpdateModel liveUpdateModel = LiveUpdateMapper.liveUpdateRequestDtoToLiveUpdateModel(liveUpdateRequestDto);
        LiveDriverEntity liveDriverEntity = liveDriverRepository
                .findById(liveUpdateModel.driverId())
                .orElse(null);

        if (liveDriverEntity == null) {
            log.error("Live Update Consumer: liveDriver not found by driverId {}", liveUpdateModel.driverId());
            return;
        }

        LiveUpdateEntity liveUpdateEntity = LiveUpdateMapper.liveUpdateModelToLiveUpdateEntity(liveUpdateModel);
        liveUpdateEntity.setDriverStatus(liveDriverEntity.getDriverStatus());
        liveUpdateEntity.setRideId(liveDriverEntity.getRideId());
        liveUpdateRepository.save(liveUpdateEntity);
    }
}
