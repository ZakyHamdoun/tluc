package com.taxinopli.tluc.mapper;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.geometry.GeometryUtils;
import com.taxinopli.tluc.model.LiveUpdateModel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class LiveUpdateMapper {
    private LiveUpdateMapper() {

    }

    public static LiveUpdateModel liveUpdateRequestDtoToLiveUpdateModel(LiveUpdateRequestDto liveUpdateRequestDto) {
        // Coordinate constructor takes X (longitude) first, then Y (latitude).
        Point location = GeometryUtils.GEOMETRY_FACTORY.createPoint(new Coordinate(
                liveUpdateRequestDto.getCoordinate().getLongitude(),
                liveUpdateRequestDto.getCoordinate().getLatitude()
        ));

        return new LiveUpdateModel(
                liveUpdateRequestDto.getTimestamp(),
                liveUpdateRequestDto.getDriverId(),
                liveUpdateRequestDto.getDriverStatus(),
                location,
                liveUpdateRequestDto.getSpeed()
        );
    }

    public static LiveUpdateEntity liveUpdateModelToLiveUpdateEntity(LiveUpdateModel liveUpdateModel) {
        LiveUpdateEntity liveUpdateEntity = new LiveUpdateEntity();
        liveUpdateEntity.setTimestamp(liveUpdateModel.timestamp());
        liveUpdateEntity.setDriverId(liveUpdateModel.driverId());
        liveUpdateEntity.setDriverStatus(liveUpdateModel.driverStatus());
        liveUpdateEntity.setLocation(liveUpdateModel.location());
        liveUpdateEntity.setSpeed(liveUpdateModel.speed());

        return liveUpdateEntity;
    }

    /**
     * Convert Object[] from native queries to LiveUpdateEntity
     * Used for PostGIS function results
     */
    public static LiveUpdateEntity objectArrayToLiveUpdateEntity(Object[] row) {
        LiveUpdateEntity entity = new LiveUpdateEntity();
        entity.setId((Long) row[0]);
        entity.setTimestamp((java.time.Instant) row[1]);
        entity.setDriverId((Long) row[2]);
        entity.setDriverStatus(com.taxinopli.tluc.enumeration.DriverStatus.valueOf((String) row[3]));
        entity.setLocation((Point) row[4]);
        entity.setSpeed((Double) row[5]);
        return entity;
    }
}