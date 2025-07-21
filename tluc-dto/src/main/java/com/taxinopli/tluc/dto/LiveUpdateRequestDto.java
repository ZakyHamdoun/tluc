package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Schema(description = "A live update.", name = "LiveUpdateInput")
public class LiveUpdateRequestDto {
    @Schema(description = "Live update's timestamp.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2025-07-21T12:30:45Z")
    @NotNull(message = "timestamp is required.")
    private Instant timestamp;

    @Schema(description = "Live update's corresponding driver's UUID.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "driverUuid is required.")
    private UUID driverUuid;

    @Schema(description = "Live update's corresponding driver's status.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "OCCUPIED")
    @NotNull(message = "driverStatus is required")
    private DriverStatus driverStatus;

    @Schema(description = "Live update's latitude.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "48.8583764")
    @NotNull(message = "latitude is required.")
    @DecimalMin(value = "-90.0", message = "latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90.0")
    private Double latitude;

    @Schema(description = "Live update's longitude.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2.2120929")
    @NotNull(message = "longitude is required.")
    @DecimalMin(value = "-180.0", message = "longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180.0")
    private Double longitude;

    @Schema(description = "Live update's speed (km/h).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "30")
    @NotNull(message = "speed is required")
    @PositiveOrZero(message = "speed must be zero or positive")
    private Double speed;
}
