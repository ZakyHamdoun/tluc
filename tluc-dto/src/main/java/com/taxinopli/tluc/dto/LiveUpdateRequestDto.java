package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.Instant;

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
            example = "22555")
    @NotNull(message = "driverId is required.")
    private Long driverId;

    @Schema(description = "Live update's corresponding driver's status.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "OCCUPIED")
    @NotNull(message = "driverStatus is required")
    private DriverStatus driverStatus;

    @Schema(description = "Live update's coordinate.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = """
                    {"longitude": 2.2120929, "latitude": 48.8583764}
                    """)
    @NotNull(message = "coordinate is required.")
    private @Valid CoordinateDto coordinate;

    @Schema(description = "Live update's speed (km/h).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "30")
    @NotNull(message = "speed is required")
    @PositiveOrZero(message = "speed must be zero or positive")
    private Double speed;
}
