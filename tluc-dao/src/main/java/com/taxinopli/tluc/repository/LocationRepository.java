package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveUpdateEntity;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LiveUpdateEntity, Long> {
    @Query(value = """
        SELECT DISTINCT ON (driver_id) *
        FROM tluc.live_updates
        WHERE timestamp BETWEEN :from AND :till
            AND ST_DWithin(
                location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :radius
            )
            AND (:driverStatus IS NULL OR driver_status = :driverStatus)
            AND (:minSpeed IS NULL OR speed >= :minSpeed)
            AND (:maxSpeed IS NULL OR speed <= :maxSpeed)
        ORDER BY driver_id, timestamp DESC;
        """,
            nativeQuery = true
    )
    List<LiveUpdateEntity> findNearbyDriversByPoint(
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius") double radius,
            @Param("from") Instant from,
            @Param("till") Instant till,
            @Param("driverStatus") String driverStatus,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed);

    @Query(value = """
        SELECT DISTINCT ON (driver_id) *
        FROM tluc.live_updates
        WHERE timestamp BETWEEN :from AND :till
            AND ST_Contains(:polygon, location)
            AND (:driverStatus IS NULL OR driver_status = :driverStatus)
            AND (:minSpeed IS NULL OR speed >= :minSpeed)
            AND (:maxSpeed IS NULL OR speed <= :maxSpeed)
        ORDER BY driver_id, timestamp DESC;
        """,
            nativeQuery = true
    )
    List<LiveUpdateEntity> findDriversInPolygon(
            @Param("polygon") Polygon polygon,
            @Param("from") Instant from,
            @Param("till") Instant till,
            @Param("driverStatus") String driverStatus,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed
    );
}