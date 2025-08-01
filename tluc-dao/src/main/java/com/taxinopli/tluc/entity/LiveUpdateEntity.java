package com.taxinopli.tluc.entity;

import com.taxinopli.tluc.enumeration.DriverStatus;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "live_updates", schema = "tluc")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class LiveUpdateEntity {
    @EmbeddedId
    private LiveUpdateId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_status", nullable = false)
    private DriverStatus driverStatus;

    @Column(name = "location", nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Column(name = "speed", nullable = false)
    private Double speed;

    @Column(name = "driver_ride_id")
    private Long driverRideId;

    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class LiveUpdateId implements Serializable {
        @Column(name = "driver_id", nullable = false)
        private Long driverId;

        @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMPTZ")
        private Instant timestamp;
    }
}