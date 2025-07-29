package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveStatusRequestDto;
import com.taxinopli.tluc.entity.LiveStatusEntity;
import com.taxinopli.tluc.mapper.LiveStatusMapper;
import com.taxinopli.tluc.model.LiveStatusModel;
import com.taxinopli.tluc.repository.LiveStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LiveStatusService {
    @Autowired
    private final LiveStatusRepository liveStatusRepository;

    @Transactional
    public void updateLiveStatus(Long driverId, LiveStatusRequestDto liveStatusRequestDto) {
        LiveStatusModel liveStatusModel = LiveStatusMapper.liveStatusRequestDtoToLiveStatusModel(
                driverId, liveStatusRequestDto);
        LiveStatusEntity liveStatusEntity = LiveStatusMapper.liveStatusModelToLiveStatusEntity(liveStatusModel);

        liveStatusRepository.save(liveStatusEntity);
    }
}
