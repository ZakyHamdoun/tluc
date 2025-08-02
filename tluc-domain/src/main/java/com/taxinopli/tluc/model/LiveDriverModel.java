package com.taxinopli.tluc.model;

import com.taxinopli.tluc.enumeration.DriverStatus;

public record LiveDriverModel(
        Long driverId,
        DriverStatus driverStatus,
        Long rideId
) {}