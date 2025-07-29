package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.mapper.LiveUpdateMapper;
import com.taxinopli.tluc.model.LiveUpdateModel;
import com.taxinopli.tluc.repository.LiveUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LiveUpdateService {
    @Autowired
    private final LiveUpdateRepository liveUpdateRepository;

    @Transactional
    public void driverForceUpdate(LiveUpdateRequestDto liveUpdateRequestDto) {
        LiveUpdateModel liveUpdateModel = LiveUpdateMapper.liveUpdateRequestDtoToLiveUpdateModel(liveUpdateRequestDto);
        LiveUpdateEntity liveUpdateEntity = LiveUpdateMapper.liveUpdateModelToLiveUpdateEntity(liveUpdateModel);
        liveUpdateRepository.save(liveUpdateEntity);
    }
}
