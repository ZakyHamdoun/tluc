package com.taxinopli.tluc.mapper;

import com.taxinopli.tluc.dto.CoordinateDto;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.entity.LocationEntity;
import com.taxinopli.tluc.model.LocationModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationMapper {
    private LocationMapper() {

    }

    public static LocationEntity liveUpdateEntityToLocationEntity(LiveUpdateEntity liveUpdateEntity) {
        Long driverId = liveUpdateEntity.getId().getDriverId();
        return new LocationEntity(driverId, liveUpdateEntity.getLocation());
    }

    // Usage of .parallelStream to be studied.
    public static List<LocationEntity> liveUpdateEntitiesToLocationEntities(
            List<LiveUpdateEntity> liveUpdateEntities)
    {
        return liveUpdateEntities
                .stream()
                .unordered()
                .map(LocationMapper::liveUpdateEntityToLocationEntity)
                .toList();
    }

    public static LocationModel locationEntityToLocationModel(LocationEntity locationEntity) {
        return new LocationModel(locationEntity.getDriverId(), locationEntity.getLocation());
    }

    public static List<LocationModel> locationEntitiesToLocationModels(
            List<LocationEntity> locationEntities) {
        return locationEntities
                .stream()
                .unordered()
                .map(LocationMapper::locationEntityToLocationModel)
                .toList();
    }

    // Coordinate constructor takes X (longitude) first, then Y (latitude).
    public static LocationResponseDto locationModelToLocationResponseDto(LocationModel locationModel) {
        CoordinateDto coordinateDto = new CoordinateDto(
                locationModel.location().getX(), locationModel.location().getY());
        return new LocationResponseDto(
                locationModel.driverId(),
                coordinateDto);
    }

    public static List<LocationResponseDto> locationModelsToLocationResponseDtos(
            List<LocationModel> locationModels) {
        return locationModels
                .stream()
                .unordered()
                .map(LocationMapper::locationModelToLocationResponseDto)
                .toList();
    }
}
