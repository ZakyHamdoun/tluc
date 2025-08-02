package com.taxinopli.tluc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class LocationEntity {
    private Long driverId;
    private Instant timestamp;
    private Point location;
}
