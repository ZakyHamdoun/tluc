package com.taxinopli.tluc.dto;

import com.taxinopli.tluc.enumeration.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A live status.", name = "LiveStatusInput")
public class LiveStatusRequestDto {
    @Schema(description = "Live update's corresponding driver's status.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "OCCUPIED")
    @NotNull(message = "driverStatus is required")
    private DriverStatus driverStatus;
    
    @Schema(description = "Driver's current ride ID",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "12345")
    private Long driverRideId;
}
