package com.taxinopli.tluc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "A coordinate.", name = "Coordinate")
@AllArgsConstructor
public class CoordinateDto {
    @Schema(description = "Longitude", requiredMode = Schema.RequiredMode.REQUIRED)
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private double longitude;

    @Schema(description = "Latitude", requiredMode = Schema.RequiredMode.REQUIRED)
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private double latitude;
}
