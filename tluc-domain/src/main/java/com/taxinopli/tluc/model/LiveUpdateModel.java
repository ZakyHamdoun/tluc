package com.taxinopli.tluc.model;

import org.locationtech.jts.geom.Point;

import java.time.Instant;

public record LiveUpdateModel(
        Instant timestamp,
        Long driverId,
        Point location,
        Double speed
) {}
