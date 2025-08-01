package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Schema(description = "A request drivers in polygon by duration.", name = "DriversPolyDurationInput")
public class DriversInPolyDurationRequestDto {
    @Schema(description = "List of points forming the polygon. Must contain at least 3 points.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = """
                    [{"longitude": 2.294481, "latitude": 48.858370}, {"longitude": 2.294481, "latitude": 48.858370},
                    {"longitude": 2.294481, "latitude": 48.858370}]
                    """)
    @NotNull(message = "poly points are required.")
    @Size(min = 3, message = "At least 3 points are required to form a polygon.")
    private List<@Valid CoordinateDto> poly;

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

    @Schema(description = "Duration (seconds).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "3600")
    @NotNull(message = "duration is required.")
    private Long duration;

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
