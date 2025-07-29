package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.enumeration.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LiveUpdateEntity, Long> {
    @Query(value = """
        SELECT *,
            first(location, timestamp)
        FROM tluc.live_updates
        WHERE ST_DWithin(
                location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :radius
            )
        AND timestamp > NOW() - INTERVAL ':maximumAge minutes'
        AND (:driverStatus IS NULL OR driver_status = :driverStatus)
        AND (:minSpeed IS NULL OR speed >= :minSpeed)
        AND (:maxSpeed IS NULL OR speed <= :maxSpeed);
        """,
            nativeQuery = true
    )
    List<LiveUpdateEntity> findNearbyDriversByPoint(
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius") double radius,
            @Param("maximumAge") double maximumAge,
            @Param("driverStatus") DriverStatus driverStatus,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed);

    @Query(value = """
        SELECT *,
            first(location, timestamp)
        FROM tluc.live_updates
        WHERE ST_Contains(
                ST_SetSRID(
                    ST_MakePolygon(
                        ST_MakeLine(ARRAY[
                            :pointArray
                        ]::geometry[])), 4326),
                location
            )
        AND timestamp > NOW() - INTERVAL ':maximumAge minutes'
        AND (:driverStatus IS NULL OR driver_status = :driverStatus)
        AND (:minSpeed IS NULL OR speed >= :minSpeed)
        AND (:maxSpeed IS NULL OR speed <= :maxSpeed);
        """,
            nativeQuery = true
    )
    List<LiveUpdateEntity> findDriversInPolygon(
            @Param("pointArray") String pointArray,
            @Param("maximumAge") double maximumAge,
            @Param("driverStatus") DriverStatus driverStatus,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed
    );
}