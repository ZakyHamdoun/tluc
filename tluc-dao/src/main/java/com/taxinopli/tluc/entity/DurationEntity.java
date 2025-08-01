package com.taxinopli.tluc.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DurationEntity {
    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "first_seen", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private Instant firstSeen;

    @Column(name = "last_seen", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private Instant lastSeen;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @Column(name = "average_speed", nullable = false)
    private Double averageSpeed;
}
