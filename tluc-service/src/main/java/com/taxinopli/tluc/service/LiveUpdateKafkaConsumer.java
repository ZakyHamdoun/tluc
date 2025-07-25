package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.enumeration.DriverStatus;
import com.taxinopli.tluc.mapper.LiveUpdateMapper;
import com.taxinopli.tluc.model.LiveUpdateModel;
import com.taxinopli.tluc.repository.LiveUpdateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Unified Real-time Location Service
 * This service handles both Kafka consumption AND business logic for live updates
 * Single responsibility: Process real-time taxi location data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateKafkaConsumer {

    private final LiveUpdateRepository liveUpdateRepository;

    /**
     * Main Kafka Consumer - the heart of the real-time system
     * Consumes location updates and saves them to TimescaleDB
     */
    @KafkaListener(
        topics = "taxi-live-update",
        groupId = "tluc-live-update-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consumeLiveUpdate(
            @Payload LiveUpdateRequestDto liveUpdateDto,
            @Header(KafkaHeaders.RECEIVED_KEY) String driverKey,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Consumed taxi location update from Kafka - Driver: {}, Topic: {}, Partition: {}", 
                    driverKey, topic, partition);
            
            LiveUpdateModel savedModel = processLocationUpdate(liveUpdateDto);
            
            log.info("Successfully saved taxi location update to TimescaleDB for driver: {}", 
                    liveUpdateDto.getDriverUuid());
            
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing taxi location update for driver: {}", driverKey, e);
        }
    }

    /**
     * Core business logic: Process location update
     * Follows layered architecture - business layer works with DTOs and Models only
     * No entity instances created in business layer!
     */
    @Transactional
    public LiveUpdateModel processLocationUpdate(LiveUpdateRequestDto liveUpdateDto) {
        log.debug("Processing taxi location update for driver: {}", liveUpdateDto.getDriverUuid());
        
        LiveUpdateModel liveUpdateModel = LiveUpdateMapper.liveUpdateRequestDtoToLiveUpdateModel(liveUpdateDto);
        
        return LiveUpdateMapper.liveUpdateEntityToLiveUpdateModel(
            liveUpdateRepository.save(
                LiveUpdateMapper.liveUpdateModelToLiveUpdateEntity(liveUpdateModel)
            )
        );
    }

    // ======================== SUIIIIII QUERY METHODS (Business Logic) ==========================

    /**
     * Get the latest location for a specific driver
     */
    @Transactional(readOnly = true)
    public Optional<LiveUpdateModel> getDriverLatestLocation(UUID driverUuid) {
        log.debug("Fetching latest location for driver: {}", driverUuid);
        
        return liveUpdateRepository.findFirstByDriverUuidOrderByTimestampDesc(driverUuid)
                .map(LiveUpdateMapper::liveUpdateEntityToLiveUpdateModel);
    }

    /**
     * Get location history for a driver within a time range
     */
    @Transactional(readOnly = true)
    public List<LiveUpdateModel> getDriverLocationHistory(UUID driverUuid, int hoursBack) {
        log.debug("Fetching location history for driver: {} for last {} hours", driverUuid, hoursBack);
        
        Instant startTime = Instant.now().minus(hoursBack, ChronoUnit.HOURS);
        Instant endTime = Instant.now();
        
        return liveUpdateRepository.findByDriverUuidAndTimestampBetween(driverUuid, startTime, endTime)
                .stream()
                .map(LiveUpdateMapper::liveUpdateEntityToLiveUpdateModel)
                .toList();
    }

    /**
     * Get all active drivers (with updates in the last N minutes)
     */
    @Transactional(readOnly = true)
    public List<UUID> getActiveDrivers(int minutesBack) {
        log.debug("Fetching active drivers from last {} minutes", minutesBack);
        
        Instant since = Instant.now().minus(minutesBack, ChronoUnit.MINUTES);
        return liveUpdateRepository.findActiveDriversSince(since);
    }

    /**
     * Get drivers by status with recent updates
     */
    @Transactional(readOnly = true)
    public List<LiveUpdateModel> getDriversByStatus(DriverStatus status, int minutesBack) {
        log.debug("Fetching drivers with status: {} from last {} minutes", status, minutesBack);
        
        Instant since = Instant.now().minus(minutesBack, ChronoUnit.MINUTES);
        return liveUpdateRepository.findByDriverStatusAndTimestampAfter(status, since)
                .stream()
                .map(LiveUpdateMapper::liveUpdateEntityToLiveUpdateModel)
                .toList();
    }

    /**
     * Count total updates for a driver in a specific time period
     */
    @Transactional(readOnly = true)
    public Long getDriverUpdateCount(UUID driverUuid, int hoursBack) {
        log.debug("Counting updates for driver: {} in last {} hours", driverUuid, hoursBack);
        
        Instant startTime = Instant.now().minus(hoursBack, ChronoUnit.HOURS);
        Instant endTime = Instant.now();
        
        return liveUpdateRepository.countByDriverUuidAndTimestampBetween(driverUuid, startTime, endTime);
    }

        /**
     * Get nearby drivers using PostGIS ST_DWithin function
     */
    @Transactional(readOnly = true)
    public List<LiveUpdateModel> getNearbyDrivers(double latitude, double longitude, double distanceMeters, int maxAgeMinutes) {
        log.debug("Fetching nearby drivers within {} meters from lat: {}, lon: {} with max age {} minutes", 
                distanceMeters, latitude, longitude, maxAgeMinutes);
        
        return liveUpdateRepository.findNearbyDrivers(latitude, longitude, distanceMeters, maxAgeMinutes)
                .stream()
                .map(LiveUpdateMapper::liveUpdateEntityToLiveUpdateModel)
                .toList();
    }
    
    /**
     * Get drivers within a bounding box (useful for map viewport queries)
     */
    @Transactional(readOnly = true)
    public List<LiveUpdateModel> getDriversInBoundingBox(double minLat, double minLon, double maxLat, double maxLon, int maxAgeMinutes) {
        log.debug("Fetching drivers in bounding box: ({}, {}) to ({}, {}) with max age {} minutes", 
                minLat, minLon, maxLat, maxLon, maxAgeMinutes);
        
        return liveUpdateRepository.findDriversInBoundingBox(minLat, minLon, maxLat, maxLon, maxAgeMinutes)
                .stream()
                .map(LiveUpdateMapper::liveUpdateEntityToLiveUpdateModel)
                .toList();
    }
}
