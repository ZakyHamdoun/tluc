package com.taxinopli.tluc.model;

import org.locationtech.jts.geom.Point;

public record LocationModel(
        Long driverId,
        Point location
) {}
