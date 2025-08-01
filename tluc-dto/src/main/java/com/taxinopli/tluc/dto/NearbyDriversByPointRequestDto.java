package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "A request for nearby drivers location update.", name = "NearbyDriversInput")
public class NearbyDriversByPointRequestDto {
    @Schema(description = "Center's coordinate.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "{2.294481, 48.858370}")
    @NotNull(message = "coordinate is required.")
    private @Valid CoordinateDto coordinate;

    @Schema(description = "Radius (m).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "3000")
    @NotNull(message = "radius is required.")
    @DecimalMin(value = "1", message = "radius must be >= 1")
    @DecimalMax(value = "5000", message = "radius must be <= 5000")
    private double radius;

    @Schema(description = "From (Time-zoned timestamp).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2025-07-21 14:30:45+02")
    @NotNull(message = "from is required.")
    private Instant from;

    @Schema(description = "Till (Time-zoned timestamp).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2025-07-21 14:30:45+02")
    @NotNull(message = "till is required.")
    private Instant till;

    @Schema(description = "Filter by driver status.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "OCCUPIED", defaultValue = "null")
    private DriverStatus driverStatus;

    @Schema(description = "Filter by minimum speed (km/h).",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "30", defaultValue = "null")
    @PositiveOrZero(message = "minSpeed must be either zero, positive or null")
    private Double minSpeed;

    @Schema(description = "Filter by maximum speed (km/h).",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "30", defaultValue = "null")
    @PositiveOrZero(message = "maxSpeed must be either zero, positive or null")
    private Double maxSpeed;
}
