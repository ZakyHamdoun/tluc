package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveStatusRequestDto;
import com.taxinopli.tluc.enumeration.DriverStatus;
import com.taxinopli.tluc.mapper.LiveStatusMapper;
import com.taxinopli.tluc.model.LiveStatusModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveStatusService {
    
    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String DRIVER_STATUS_KEY_PREFIX = "driver:status:";
    private static final Duration DEFAULT_TTL = Duration.ofHours(24);
    
    /**
     * Updates or creates the driver's status in Redis cache, if the driver does not exist, it will create a new entry in the Redis Cache.
     * If the driver already exists, it will update the status and ride ID.
     */
    public void updateDriverStatus(Long driverId, DriverStatus driverStatus, Long driverRideId) {
        try {
            String key = DRIVER_STATUS_KEY_PREFIX + driverId;

            Map<String, Object> statusInfo = new HashMap<>();

            statusInfo.put("status", driverStatus.toString());
            statusInfo.put("rideId", driverRideId);
            statusInfo.put("updatedAt", System.currentTimeMillis());

            redisTemplate.opsForValue().set(key, statusInfo, DEFAULT_TTL);
            
            log.debug("Driver status updated in Redis for driverId: {}, status: {}, rideId: {}", 
                     driverId, driverStatus, driverRideId);
                     
        } catch (Exception e) {
            log.error("Error updating driver status in Redis for driverId: {}", driverId, e);
        }
    }
    
    /**
     * Updates the driver's status in Redis cache, main function to be called
     */
    public void updateDriverStatus(Long driverId, LiveStatusRequestDto liveStatusRequestDto) {
        LiveStatusModel liveStatusModel = LiveStatusMapper.liveStatusRequestDtoToLiveStatusModel(
                driverId, liveStatusRequestDto);
        
        DriverStatus newDriverStatus = liveStatusModel.driverStatus();
        Long newRideId = liveStatusModel.driverRideId();

        log.info("Updating driver status in Redis for driverId: {}", driverId);
        
        updateDriverStatus(driverId, newDriverStatus, newRideId);
    }

    /**
     * Retrieves the driver's status from Redis cache
     */
    public Optional<DriverStatusInfo> getDriverStatus(Long driverId) {
        try {

            log.info("Retrieving driver status from Redis for driverId: {}", driverId);

            String key = DRIVER_STATUS_KEY_PREFIX + driverId;
            Object value = redisTemplate.opsForValue().get(key);
            
            if (value == null) {
                return Optional.empty();
            }
            
            // Si c'est déjà un Map (depuis JSON)
            if (value instanceof Map) {
                
                log.info("Driver status found in Redis for driverId: {}", driverId);

                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                return Optional.of(new DriverStatusInfo(
                    DriverStatus.valueOf((String) map.get("status")),
                    map.get("rideId") != null ? Long.valueOf(map.get("rideId").toString()) : null
                ));
            }
            
            log.warn("Unexpected value type for driver {}: {}", driverId, value.getClass());
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error retrieving driver status from Redis for driverId: {}", driverId, e);
            return Optional.empty();
        }
    }
    
    /**
     * Check if driver exists in cache
     */
    public boolean driverExists(Long driverId) {
        try {
            String key = DRIVER_STATUS_KEY_PREFIX + driverId;

            log.info("Checking existence of driver in Redis for driverId: {}", driverId);

            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking driver existence in Redis for driverId: {}", driverId, e);
            return false;
        }
    }
    
    /**
     * Record to encapsulate status information with getters
     */
    public record DriverStatusInfo(DriverStatus status, Long rideId) {}
}
