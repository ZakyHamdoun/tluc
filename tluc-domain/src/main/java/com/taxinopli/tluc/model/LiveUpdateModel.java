package com.taxinopli.tluc.model;

import com.taxinopli.tluc.enumeration.DriverStatus;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

public record LiveUpdateModel(
    Instant timestamp,
    UUID driverUuid,
    DriverStatus driverStatus,
    Point location,
    Double speed
) {}
