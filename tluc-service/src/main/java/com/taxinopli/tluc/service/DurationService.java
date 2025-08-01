package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.*;
import com.taxinopli.tluc.entity.DurationEntity;
import com.taxinopli.tluc.geometry.GeometryUtils;
import com.taxinopli.tluc.mapper.DurationMapper;
import com.taxinopli.tluc.model.DurationModel;
import com.taxinopli.tluc.repository.DurationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DurationService {
    @Autowired
    private final DurationRepository durationRepository;

    @Transactional(readOnly = true)
    public List<DurationResponseDto> getNearbyDriversByPointDuration(
            NearbyDriversByPointDurationRequestDto nearbyDriversByPointDurationRequestDto) {
        List<Object[]> objects = durationRepository.findNearbyDriversByPointDuration(
                nearbyDriversByPointDurationRequestDto.getCoordinate().getLongitude(),
                nearbyDriversByPointDurationRequestDto.getCoordinate().getLatitude(),
                nearbyDriversByPointDurationRequestDto.getRadius(),
                nearbyDriversByPointDurationRequestDto.getFrom(),
                nearbyDriversByPointDurationRequestDto.getTill(),
                nearbyDriversByPointDurationRequestDto.getDuration(),
                nearbyDriversByPointDurationRequestDto.getDriverStatus().name(),
                nearbyDriversByPointDurationRequestDto.getMinSpeed(),
                nearbyDriversByPointDurationRequestDto.getMaxSpeed()
        );
        List<DurationEntity> durationEntities = DurationMapper.objectsToDurationEntities(objects);
        List<DurationModel> durationModels = DurationMapper.durationEntitiesToDurationModels(durationEntities);

        return DurationMapper.durationModelsToDurationResponseDtos(
                durationModels);
    }

    @Transactional(readOnly = true)
    public List<DurationResponseDto> getDriversInPolygonDuration(
            DriversInPolyDurationRequestDto driversInPolyDurationRequestDto) {
        Coordinate[] polyCoordinates = driversInPolyDurationRequestDto.getPoly().stream()
                .map(p -> new Coordinate(p.getLongitude(), p.getLatitude()))
                .toArray(Coordinate[]::new);
        Polygon polygon = GeometryUtils.GEOMETRY_FACTORY.createPolygon(polyCoordinates);
        List<Object[]> objects = durationRepository.findDriversInPolygonDuration(
                polygon,
                driversInPolyDurationRequestDto.getFrom(),
                driversInPolyDurationRequestDto.getTill(),
                driversInPolyDurationRequestDto.getDuration(),
                driversInPolyDurationRequestDto.getDriverStatus().name(),
                driversInPolyDurationRequestDto.getMinSpeed(),
                driversInPolyDurationRequestDto.getMaxSpeed()
        );
        List<DurationEntity> durationEntities = DurationMapper.objectsToDurationEntities(objects);
        List<DurationModel> durationModels = DurationMapper.durationEntitiesToDurationModels(durationEntities);

        return DurationMapper.durationModelsToDurationResponseDtos(
                durationModels);
    }
}
