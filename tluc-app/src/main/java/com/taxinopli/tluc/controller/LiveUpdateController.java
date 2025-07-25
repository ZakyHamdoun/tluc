package com.taxinopli.tluc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.model.LiveUpdateModel;
import com.taxinopli.tluc.service.LiveUpdateKafkaConsumer;
import com.taxinopli.tluc.service.LiveUpdateKafkaProducer;
import com.taxinopli.tluc.service.RealtimeMonitoringService;
import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/realtime", produces = APPLICATION_JSON_VALUE)
@Tag(name = "1. Live Update", description = "Everything related to live updates.")
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateController {

    private final LiveUpdateKafkaConsumer realtimeService;
    private final LiveUpdateKafkaProducer kafkaProducer;
    private final RealtimeMonitoringService monitoringService;

    @MessageMapping("/driver-update")
    @SendTo("/topic/driver-updates")
    @Operation(summary="Taxi Location Update via WebSocket", description="Receive real-time taxi location and status update")
    public LiveUpdateModel handleDriverUpdate(@Valid LiveUpdateRequestDto liveUpdateDto){
        log.info("WebSocket: Received taxi location update for driver: {}", liveUpdateDto.getDriverUuid());

        LiveUpdateModel savedData = realtimeService.processLocationUpdate(liveUpdateDto);

        kafkaProducer.sendToKafka(liveUpdateDto);
        
        log.info("WebSocket: Taxi location update sent to Kafka for driver: {}", liveUpdateDto.getDriverUuid());
        
        return savedData;
    }

    @GetMapping("/driver/{driverUuid}/latest")
    @Operation(summary="Get Driver Latest Location", description="Get the most recent location update for a specific driver")
    public ResponseEntity<LiveUpdateModel> getDriverLatestLocation(@PathVariable UUID driverUuid) {
        log.debug("Fetching latest location for driver: {}", driverUuid);
        
        Optional<LiveUpdateModel> latestLocation = realtimeService.getDriverLatestLocation(driverUuid);
        
        return latestLocation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverUuid}/history")
    @Operation(summary="Get Driver Location History", description="Get location history for a driver")
    public ResponseEntity<List<LiveUpdateModel>> getDriverLocationHistory(
            @PathVariable UUID driverUuid,
            @RequestParam(defaultValue = "24") int hoursBack) {
        
        log.debug("Fetching location history for driver: {} for last {} hours", driverUuid, hoursBack);
        
        List<LiveUpdateModel> history = realtimeService.getDriverLocationHistory(driverUuid, hoursBack);
        
        return ResponseEntity.ok(history);
    }

    @GetMapping("/drivers/active")
    @Operation(summary="Get Active Drivers", description="Get list of drivers with recent updates")
    public ResponseEntity<List<UUID>> getActiveDrivers(
            @RequestParam(defaultValue = "5") int minutesBack) {
        
        log.debug("Fetching active drivers from last {} minutes", minutesBack);
        
        List<UUID> activeDrivers = realtimeService.getActiveDrivers(minutesBack);
        
        return ResponseEntity.ok(activeDrivers);
    }

    @GetMapping("/drivers/by-status/{status}")
    @Operation(summary="Get Drivers by Status", description="Get drivers filtered by their current status")
    public ResponseEntity<List<LiveUpdateModel>> getDriversByStatus(
            @PathVariable DriverStatus status,
            @RequestParam(defaultValue = "5") int minutesBack) {
        
        log.debug("Fetching drivers with status: {} from last {} minutes", status, minutesBack);
        
        List<LiveUpdateModel> drivers = realtimeService.getDriversByStatus(status, minutesBack);
        
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/driver/{driverUuid}/stats")
    @Operation(summary="Get Driver Update Statistics", description="Get update count statistics for a driver")
    public ResponseEntity<DriverStatsResponse> getDriverStats(
            @PathVariable UUID driverUuid,
            @RequestParam(defaultValue = "24") int hoursBack) {
        
        log.debug("Fetching update statistics for driver: {} for last {} hours", driverUuid, hoursBack);
        
        Long updateCount = realtimeService.getDriverUpdateCount(driverUuid, hoursBack);
        
        DriverStatsResponse stats = new DriverStatsResponse(driverUuid, updateCount, hoursBack);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    @Operation(summary="System Health Check", description="Check the health of the real-time system components")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        log.debug("Checking system health");
        
        Map<String, Object> health = monitoringService.getSystemHealth();
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/drivers/nearby")
    @Operation(summary="Get Nearby Drivers", description="Get drivers within a specific distance using PostGIS")
    public ResponseEntity<List<LiveUpdateModel>> getNearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "1000") double distanceMeters,
            @RequestParam(defaultValue = "5") int maxAgeMinutes) {
        
        log.debug("Fetching nearby drivers within {} meters from lat: {}, lon: {}", 
                distanceMeters, latitude, longitude);
        
        List<LiveUpdateModel> nearbyDrivers = realtimeService.getNearbyDrivers(
                latitude, longitude, distanceMeters, maxAgeMinutes);
        
        return ResponseEntity.ok(nearbyDrivers);
    }

    @GetMapping("/drivers/in-bounds")
    @Operation(summary="Get Drivers in Bounding Box", description="Get drivers within a geographical bounding box")
    public ResponseEntity<List<LiveUpdateModel>> getDriversInBounds(
            @RequestParam double minLat,
            @RequestParam double minLon,
            @RequestParam double maxLat,
            @RequestParam double maxLon,
            @RequestParam(defaultValue = "5") int maxAgeMinutes) {
        
        log.debug("Fetching drivers in bounding box: ({}, {}) to ({}, {})", 
                minLat, minLon, maxLat, maxLon);
        
        List<LiveUpdateModel> driversInBounds = realtimeService.getDriversInBoundingBox(
                minLat, minLon, maxLat, maxLon, maxAgeMinutes);
        
        return ResponseEntity.ok(driversInBounds);
    }
    public record DriverStatsResponse(UUID driverUuid, Long updateCount, int hoursBack) {}
}
