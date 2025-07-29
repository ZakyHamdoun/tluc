package com.taxinopli.tluc.mapper;

import com.taxinopli.tluc.dto.LiveStatusRequestDto;
import com.taxinopli.tluc.entity.LiveStatusEntity;
import com.taxinopli.tluc.model.LiveStatusModel;
import org.springframework.stereotype.Component;

@Component
public class LiveStatusMapper {
    private LiveStatusMapper() {

    }

    public static LiveStatusModel liveStatusRequestDtoToLiveStatusModel(
            Long driverId, LiveStatusRequestDto liveStatusRequestDto) {
        return new LiveStatusModel(driverId, liveStatusRequestDto.getDriverStatus());
    }

    public static LiveStatusEntity liveStatusModelToLiveStatusEntity(LiveStatusModel liveStatusModel) {
        return new LiveStatusEntity(liveStatusModel.driverId(), liveStatusModel.driverStatus());
    }
}
