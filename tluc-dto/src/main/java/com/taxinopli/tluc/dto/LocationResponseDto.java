package com.taxinopli.tluc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "A location response.", name = "LocationOutput")
@AllArgsConstructor
public class LocationResponseDto {
    @Schema(description = "Location's corresponding driver's UUID.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "driverId is required.")
    private Long driverId;

    @Schema(description = "Center's coordinate.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = """
                    {"longitude": 2.2120929, "latitude": 48.8583764}
                    """)
    @NotNull(message = "coordinate is required.")
    private @Valid CoordinateDto coordinate;
}
