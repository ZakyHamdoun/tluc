package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.entity.LiveDriverEntity;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.mapper.LiveUpdateMapper;
import com.taxinopli.tluc.model.LiveUpdateModel;
import com.taxinopli.tluc.repository.LiveDriverRepository;
import com.taxinopli.tluc.repository.LiveUpdateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateService {
    @Autowired
    private final LiveUpdateRepository liveUpdateRepository;

    @Autowired
    private final LiveDriverRepository liveDriverRepository;

    @Transactional
    public void driverForceUpdate(LiveUpdateRequestDto liveUpdateRequestDto) {
        LiveUpdateModel liveUpdateModel = LiveUpdateMapper.liveUpdateRequestDtoToLiveUpdateModel(liveUpdateRequestDto);
        LiveDriverEntity liveDriverEntity = liveDriverRepository
                .findById(liveUpdateModel.driverId())
                .orElse(null);

        if (liveDriverEntity == null) {
            log.error("Live Update: liveDriver not found by driverId {}", liveUpdateModel.driverId());
            return;
        }

        LiveUpdateEntity liveUpdateEntity = LiveUpdateMapper.liveUpdateModelToLiveUpdateEntity(liveUpdateModel);
        liveUpdateEntity.setDriverStatus(liveDriverEntity.getDriverStatus());
        liveUpdateEntity.setRideId(liveUpdateEntity.getRideId());
        liveUpdateRepository.save(liveUpdateEntity);
    }
}
