package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.DriversInPolyRequestDto;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.dto.NearbyDriversByPointRequestDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.entity.LocationEntity;
import com.taxinopli.tluc.geometry.GeometryUtils;
import com.taxinopli.tluc.mapper.LocationMapper;
import com.taxinopli.tluc.model.LocationModel;
import com.taxinopli.tluc.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    @Autowired
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<LocationResponseDto> getNearbyDriversByPoint(
            NearbyDriversByPointRequestDto nearbyDriversByPointRequestDto) {
        List<LiveUpdateEntity> liveUpdateEntities = locationRepository.findNearbyDriversByPoint(
                nearbyDriversByPointRequestDto.getCoordinate().getLongitude(),
                nearbyDriversByPointRequestDto.getCoordinate().getLatitude(),
                nearbyDriversByPointRequestDto.getRadius(),
                nearbyDriversByPointRequestDto.getFrom(),
                nearbyDriversByPointRequestDto.getTill(),
                nearbyDriversByPointRequestDto.getDriverStatus().name(),
                nearbyDriversByPointRequestDto.getMinSpeed(),
                nearbyDriversByPointRequestDto.getMaxSpeed()
        );
        List<LocationEntity> locationEntities = LocationMapper.liveUpdateEntitiesToLocationEntities(liveUpdateEntities);
        List<LocationModel> locationModels = LocationMapper.locationEntitiesToLocationModels(locationEntities);

        return LocationMapper.locationModelsToLocationResponseDtos(
                locationModels);
    }

    @Transactional(readOnly = true)
    public List<LocationResponseDto> getDriversInPolygon(
            DriversInPolyRequestDto driversInPolyRequestDto) {
        Coordinate[] polyCoordinates = driversInPolyRequestDto.getPoly().stream()
                .map(p -> new Coordinate(p.getLongitude(), p.getLatitude()))
                .toArray(Coordinate[]::new);
        Polygon polygon = GeometryUtils.GEOMETRY_FACTORY.createPolygon(polyCoordinates);
        List<LiveUpdateEntity> liveUpdateEntities = locationRepository.findDriversInPolygon(
                polygon,
                driversInPolyRequestDto.getFrom(),
                driversInPolyRequestDto.getTill(),
                driversInPolyRequestDto.getDriverStatus().name(),
                driversInPolyRequestDto.getMinSpeed(),
                driversInPolyRequestDto.getMaxSpeed()
        );
        List<LocationEntity> locationEntities = LocationMapper.liveUpdateEntitiesToLocationEntities(liveUpdateEntities);
        List<LocationModel> locationModels = LocationMapper.locationEntitiesToLocationModels(locationEntities);

        return LocationMapper.locationModelsToLocationResponseDtos(
                locationModels);
    }
}
