package com.taxinopli.tluc.model;

import com.taxinopli.tluc.enumeration.DriverStatus;

public record LiveStatusModel(
        Long driverId,
        DriverStatus driverStatus
) {}