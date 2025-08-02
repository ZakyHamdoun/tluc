package com.taxinopli.tluc.entity;

import com.taxinopli.tluc.enumeration.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("live_driver")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class LiveDriverEntity implements Serializable {
    @Id
    private Long driverId;

    private DriverStatus driverStatus;

    private Long rideId;
}