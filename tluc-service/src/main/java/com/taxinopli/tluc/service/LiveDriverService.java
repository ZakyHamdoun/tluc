package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveDriverRequestDto;
import com.taxinopli.tluc.entity.LiveDriverEntity;
import com.taxinopli.tluc.mapper.LiveDriverMapper;
import com.taxinopli.tluc.model.LiveDriverModel;
import com.taxinopli.tluc.repository.LiveDriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LiveDriverService {
    @Autowired
    private final LiveDriverRepository liveDriverRepository;

    @Transactional
    public void updateLiveDriver(Long driverId, LiveDriverRequestDto liveDriverRequestDto) {
        LiveDriverModel liveStatusModel = LiveDriverMapper.liveDriverRequestDtoToLiveDriverModel(
                driverId, liveDriverRequestDto);
        LiveDriverEntity liveDriverEntity = LiveDriverMapper.liveDriverModelToLiveDriverEntity(liveStatusModel);

        liveDriverRepository.save(liveDriverEntity);
    }
}
