package com.taxinopli.tluc.mapper;

import com.taxinopli.tluc.dto.LiveDriverRequestDto;
import com.taxinopli.tluc.entity.LiveDriverEntity;
import com.taxinopli.tluc.model.LiveDriverModel;
import org.springframework.stereotype.Component;

@Component
public class LiveDriverMapper {
    private LiveDriverMapper() {

    }

    public static LiveDriverModel liveDriverRequestDtoToLiveDriverModel(
            Long driverId, LiveDriverRequestDto liveDriverRequestDto) {
        return new LiveDriverModel(
                driverId,
                liveDriverRequestDto.getDriverStatus(),
                liveDriverRequestDto.getRideId());
    }

    public static LiveDriverEntity liveDriverModelToLiveDriverEntity(LiveDriverModel liveDriverModel) {
        return new LiveDriverEntity(
                liveDriverModel.driverId(),
                liveDriverModel.driverStatus(),
                liveDriverModel.rideId());
    }
}
