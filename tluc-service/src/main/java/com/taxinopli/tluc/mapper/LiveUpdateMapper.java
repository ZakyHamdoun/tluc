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
                location,
                liveUpdateRequestDto.getSpeed()
        );
    }

    public static LiveUpdateEntity liveUpdateModelToLiveUpdateEntity(LiveUpdateModel liveUpdateModel) {
        LiveUpdateEntity.LiveUpdateId liveUpdateId = new LiveUpdateEntity.LiveUpdateId();
        liveUpdateId.setDriverId(liveUpdateModel.driverId());
        liveUpdateId.setTimestamp(liveUpdateModel.timestamp());
        LiveUpdateEntity liveUpdateEntity = new LiveUpdateEntity();
        liveUpdateEntity.setId(liveUpdateId);
        liveUpdateEntity.setLocation(liveUpdateModel.location());
        liveUpdateEntity.setSpeed(liveUpdateModel.speed());

        return liveUpdateEntity;
    }
}