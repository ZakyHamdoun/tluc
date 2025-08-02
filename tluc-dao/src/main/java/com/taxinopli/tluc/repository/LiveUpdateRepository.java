package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveUpdateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveUpdateRepository extends JpaRepository<LiveUpdateEntity, Long> {
    @Query(value = "SELECT * FROM tluc.live_updates WHERE ride_id IN :rideIds", nativeQuery = true)
    List<LiveUpdateEntity> findLiveUpdatesByRideIds(@Param("rideIds") List<Long> rideIds);
}
