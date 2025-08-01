package com.taxinopli.tluc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "A duration response.", name = "DurationOutput")
@AllArgsConstructor
public class DurationResponseDto {
    @Schema(description = "Duration's corresponding driver's ID.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1")
    @NotNull(message = "driverId is required.")
    private Long driverId;

    @Schema(description = "Duration's first seen timestamp.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2025-07-21 14:30:45+02")
    @NotNull(message = "firstSeen is required.")
    private Instant firstSeen;

    @Schema(description = "Duration's last seen timestamp.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2025-07-21 14:30:45+02")
    @NotNull(message = "firstSeen is required.")
    private Instant lastSeen;

    @Schema(description = "Duration's duration (seconds).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "3600")
    @NotNull(message = "duration is required.")
    private double duration;

    @Schema(description = "Duration's average speed (km/h).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "30")
    @NotNull(message = "duration is required.")
    private double averageSpeed;
}
