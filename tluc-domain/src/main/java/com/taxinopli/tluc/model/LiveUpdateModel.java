package com.taxinopli.tluc.model;

import com.taxinopli.tluc.enumeration.DriverStatus;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

public record LiveUpdateModel(
        Instant timestamp,
        Long driverId,
        Point location,
        Double speed
) {}
