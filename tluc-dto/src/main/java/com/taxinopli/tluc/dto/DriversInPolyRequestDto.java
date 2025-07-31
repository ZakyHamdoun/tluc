package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "A request drivers in polygon.", name = "DriversPolyInput")
public class DriversInPolyRequestDto {
    @Schema(description = "List of points forming the polygon. Must contain at least 3 points.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = """
                    [{"longitude": 2.294481, "latitude": 48.858370}, {"longitude": 2.294481, "latitude": 48.858370},
                    {"longitude": 2.294481, "latitude": 48.858370}]
                    """)
    @NotNull(message = "poly points are required.")
    @Size(min = 3, message = "At least 3 points are required to form a polygon.")
    private List<@Valid CoordinateDto> poly;

    @Schema(description = "Maximum Age (min).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "5")
    @NotNull(message = "maximum age is required.")
    @DecimalMin(value = "1", message = "maximum age must be >= 1")
    @DecimalMax(value = "1440", message = "maximum age must be <= 1440")
    private double maximumAge;

    @Schema(description = "Optional driver status filter.")
    private DriverStatus driverStatus;

    @Schema(description = "Filter by minimum speed (km/h).")
    @PositiveOrZero(message = "minSpeed must be either zero, positive or null")
    private Double minSpeed;

    @Schema(description = "Filter by maximum speed (km/h).")
    @PositiveOrZero(message = "maxSpeed must be either zero, positive or null")
    private Double maxSpeed;
}
