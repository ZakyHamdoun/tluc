package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.DriversInPolyRequestDto;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.dto.NearbyDriversByPointRequestDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.entity.LocationEntity;
import com.taxinopli.tluc.mapper.LocationMapper;
import com.taxinopli.tluc.model.LocationModel;
import com.taxinopli.tluc.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                nearbyDriversByPointRequestDto.getMaximumAge(),
                nearbyDriversByPointRequestDto.getDriverStatus(),
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
        String pointArray = driversInPolyRequestDto.getPoly().stream()
                .map(c -> "ST_MakePoint(" + c.getLongitude() + ", " + c.getLongitude() + ")")
                .collect(Collectors.joining(", "));
        List<LiveUpdateEntity> liveUpdateEntities = locationRepository.findDriversInPolygon(
                pointArray,
                driversInPolyRequestDto.getMaximumAge(),
                driversInPolyRequestDto.getDriverStatus(),
                driversInPolyRequestDto.getMinSpeed(),
                driversInPolyRequestDto.getMaxSpeed()
        );

        List<LocationEntity> locationEntities = LocationMapper.liveUpdateEntitiesToLocationEntities(liveUpdateEntities);
        List<LocationModel> locationModels = LocationMapper.locationEntitiesToLocationModels(locationEntities);

        return LocationMapper.locationModelsToLocationResponseDtos(
                locationModels);
    }
}
