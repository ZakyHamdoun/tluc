package com.taxinopli.tluc.mapper;

import com.taxinopli.tluc.dto.DurationResponseDto;
import com.taxinopli.tluc.entity.DurationEntity;
import com.taxinopli.tluc.model.DurationModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DurationMapper {
    private DurationMapper() {

    }

    public static DurationEntity objectToDurationEntity(Object[] object) {
        return new DurationEntity(
                ((Number) object[0]).longValue(),
                (Instant) object[1],
                (Instant) object[2],
                ((Number) object[3]).longValue(),
                ((Number) object[4]).doubleValue()
        );
    }

    public static List<DurationEntity> objectsToDurationEntities(List<Object[]> objects) {
        return objects
                .stream()
                .unordered()
                .map(DurationMapper::objectToDurationEntity)
                .toList();
    }

    public static DurationModel durationEntityToDurationModel(DurationEntity durationEntity) {
        return new DurationModel(
                durationEntity.getDriverId(),
                durationEntity.getFirstSeen(),
                durationEntity.getLastSeen(),
                durationEntity.getDuration(),
                durationEntity.getAverageSpeed());
    }

    public static List<DurationModel> durationEntitiesToDurationModels(
            List<DurationEntity> durationEntities)
    {
        return durationEntities
                .stream()
                .unordered()
                .map(DurationMapper::durationEntityToDurationModel)
                .toList();
    }

    public static DurationResponseDto durationModelToDurationResponseDto(DurationModel durationModel) {
        return new DurationResponseDto(
                durationModel.driverId(),
                durationModel.firstSeen(),
                durationModel.lastSeen(),
                durationModel.duration(),
                durationModel.averageSpeed()
        );
    }

    public static List<DurationResponseDto> durationModelsToDurationResponseDtos(
            List<DurationModel> durationModels) {
        return durationModels
                .stream()
                .unordered()
                .map(DurationMapper::durationModelToDurationResponseDto)
                .toList();
    }
}
