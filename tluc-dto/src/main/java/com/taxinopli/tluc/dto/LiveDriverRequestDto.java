package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A live driver.", name = "LiveDriverInput")
public class LiveDriverRequestDto {
    @Schema(description = "Live driver's status.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "OCCUPIED")
    @NotNull(message = "driverStatus is required")
    private DriverStatus driverStatus;

    @Schema(description = "Live driver's ride id.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "1")
    private Long rideId;
}
