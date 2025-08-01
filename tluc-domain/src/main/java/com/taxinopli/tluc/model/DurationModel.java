package com.taxinopli.tluc.model;

import java.time.Instant;

public record DurationModel(
        Long driverId,
        Instant firstSeen,
        Instant lastSeen,
        Long duration,
        Double averageSpeed
) {}
