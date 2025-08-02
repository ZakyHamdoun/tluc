package com.taxinopli.tluc.service;

import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.entity.LiveUpdateEntity;
import com.taxinopli.tluc.mapper.LocationMapper;
import com.taxinopli.tluc.repository.LiveUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RideService {
    @Autowired
    private final LiveUpdateRepository liveUpdateRepository;

    @Transactional(readOnly = true)
    public Map<Long, List<LocationResponseDto>> getRidesLocations(List<Long> rideIds) {
        List<LiveUpdateEntity> liveUpdateEntities = liveUpdateRepository.findLiveUpdatesByRideIds(rideIds);

        Map<Long, List<LocationResponseDto>> rides = new HashMap<>();

        for (LiveUpdateEntity lu : liveUpdateEntities) {
            rides.computeIfAbsent(lu.getRideId(), rideId -> new ArrayList<>())
                    .add(LocationMapper.locationModelToLocationResponseDto(
                            LocationMapper.locationEntityToLocationModel(
                            LocationMapper.liveUpdateEntityToLocationEntity(lu)
                    )));
        }

        return rides;
    }
}
