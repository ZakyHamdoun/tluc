package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.entity.LiveUpdateEntityId;
import com.taxinopli.tluc.enumeration.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiveUpdateRepository extends JpaRepository<LiveUpdateEntity, Long> {

    /**
     * Find the latest location update for a specific driver
     */
    Optional<LiveUpdateEntity> findFirstByDriverUuidOrderByTimestampDesc(UUID driverUuid);

    /**
     * Find all location updates for a driver within a time range
     */
    @Query("SELECT lu FROM LiveUpdateEntity lu WHERE lu.driverUuid = :driverUuid " +
           "AND lu.timestamp BETWEEN :startTime AND :endTime ORDER BY lu.timestamp DESC")
    List<LiveUpdateEntity> findByDriverUuidAndTimestampBetween(
            @Param("driverUuid") UUID driverUuid,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);

    /**
     * Find all drivers with recent updates (within last N minutes)
     */
    @Query("SELECT DISTINCT lu.driverUuid FROM LiveUpdateEntity lu WHERE lu.timestamp >= :since")
    List<UUID> findActiveDriversSince(@Param("since") Instant since);

    /**
     * Find all recent location updates (for nearby driver calculations)
     */
    @Query("SELECT lu FROM LiveUpdateEntity lu WHERE lu.timestamp >= :since ORDER BY lu.timestamp DESC")
    List<LiveUpdateEntity> findRecentUpdates(@Param("since") Instant since);

    /**
     * Find drivers by status with recent updates
     */
    @Query("SELECT lu FROM LiveUpdateEntity lu WHERE lu.driverStatus = :status " +
           "AND lu.timestamp >= :since ORDER BY lu.timestamp DESC")
    List<LiveUpdateEntity> findByDriverStatusAndTimestampAfter(
            @Param("status") DriverStatus status,
            @Param("since") Instant since);

    /**
     * Get nearby drivers using PostGIS ST_DWithin function
     * Returns drivers within specified distance (in meters) from given coordinates
     * Uses GEOGRAPHY for automatic spherical distance calculations
     */
    @Query(value = """
        SELECT * FROM tluc.live_updates 
        WHERE ST_DWithin(
            location, 
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, 
            :distanceMeters
        ) 
        AND timestamp >= NOW() - INTERVAL ':maxAgeMinutes minutes'
        ORDER BY ST_Distance(location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography)
        """, nativeQuery = true)
    List<LiveUpdateEntity> findNearbyDrivers(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distanceMeters") double distanceMeters,
            @Param("maxAgeMinutes") int maxAgeMinutes);

    /**
     * Get drivers within a bounding box (for map viewport queries)
     * Converts geography to geometry for bounding box operations
     */
    @Query(value = """
        SELECT * FROM tluc.live_updates 
        WHERE ST_Within(
            location::geometry, 
            ST_MakeEnvelope(:minLon, :minLat, :maxLon, :maxLat, 4326)
        )
        AND timestamp >= NOW() - INTERVAL ':maxAgeMinutes minutes'
        ORDER BY timestamp DESC
        """, nativeQuery = true)
    List<LiveUpdateEntity> findDriversInBoundingBox(
            @Param("minLat") double minLatitude,
            @Param("minLon") double minLongitude,
            @Param("maxLat") double maxLatitude,
            @Param("maxLon") double maxLongitude,
            @Param("maxAgeMinutes") int maxAgeMinutes);

    /**
     * Count total updates for a driver in a time period
     */
    @Query("SELECT COUNT(lu) FROM LiveUpdateEntity lu WHERE lu.driverUuid = :driverUuid " +
           "AND lu.timestamp BETWEEN :startTime AND :endTime")
    Long countByDriverUuidAndTimestampBetween(
            @Param("driverUuid") UUID driverUuid,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);
}
