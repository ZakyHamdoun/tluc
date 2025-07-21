package com.taxinopli.tluc.mapper;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.geometry.GeometryUtils;
import com.taxinopli.tluc.model.LiveUpdateModel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class LiveUpdateMapper {
    private LiveUpdateMapper() {
    }

    public static LiveUpdateModel liveUpdateRequestDtoToLiveUpdateModel(LiveUpdateRequestDto liveUpdateRequestDto) {
        GeometryFactory geometryFactory = GeometryUtils.GEOMETRY_FACTORY;
        Point liveUpdateLocation = geometryFactory.createPoint(new Coordinate(
                liveUpdateRequestDto.getLatitude(),
                liveUpdateRequestDto.getLongitude()
        ));

        return new LiveUpdateModel(
                liveUpdateRequestDto.getTimestamp(),
                liveUpdateRequestDto.getDriverUuid(),
                liveUpdateRequestDto.getDriverStatus(),
                liveUpdateLocation,
                liveUpdateRequestDto.getSpeed()
        );
    }

    public static LiveUpdateEntity liveUpdateModelToLiveUpdateEntity(LiveUpdateModel liveUpdateModel) {
        LiveUpdateEntity liveUpdateEntity = new LiveUpdateEntity();
        liveUpdateEntity.setTimestamp(liveUpdateModel.timestamp());
        liveUpdateEntity.setDriverUuid(liveUpdateModel.driverUuid());
        liveUpdateEntity.setDriverStatus(liveUpdateModel.driverStatus());
        liveUpdateEntity.setLocation(liveUpdateModel.location());
        liveUpdateEntity.setSpeed(liveUpdateEntity.getSpeed());

        return liveUpdateEntity;
    }

    public static LiveUpdateModel liveUpdateEntityToLiveUpdateModel(LiveUpdateEntity liveUpdateEntity) {
        return new LiveUpdateModel(
                liveUpdateEntity.getTimestamp(),
                liveUpdateEntity.getDriverUuid(),
                liveUpdateEntity.getDriverStatus(),
                liveUpdateEntity.getLocation(),
                liveUpdateEntity.getSpeed()
        );
    }
}