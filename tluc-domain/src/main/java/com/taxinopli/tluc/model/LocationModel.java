package com.taxinopli.tluc.model;

import org.locationtech.jts.geom.Point;

import java.time.Instant;

public record LocationModel(
        Long driverId,
        Instant timestamp,
        Point location
) {}
