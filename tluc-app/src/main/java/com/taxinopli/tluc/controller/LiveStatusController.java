package com.taxinopli.tluc.controller;

import com.taxinopli.tluc.constant.ResponseStatusHttp;
import com.taxinopli.tluc.dto.LiveStatusRequestDto;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.service.LiveStatusService;
import com.taxinopli.tluc.service.LiveStatusService.DriverStatusInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = LiveStatusController.STATUS_MAPPING, produces = APPLICATION_JSON_VALUE)
@Tag(name = "2. Status", description = "All needed operations for status updates.")
@RequiredArgsConstructor
public class LiveStatusController {
    static final String STATUS_MAPPING = "/status";
    private static final String BY_ID_MAPPING = "/{driverId}";

    @Autowired
    private final LiveStatusService liveStatusService;

    @GetMapping(path = BY_ID_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "getStatus", summary = "Get driver status by ID.",
            description = "Retrieve a driver's current status and ride ID from cache.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DriverStatusInfo.class))),
            @ApiResponse(responseCode = "404", description = "Driver status not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content)
    })
    public ResponseEntity<DriverStatusInfo> getDriverStatus(@PathVariable @Valid Long driverId) {
        Optional<DriverStatusInfo> statusInfo = liveStatusService.getDriverStatus(driverId);
        
        if (statusInfo.isPresent()) {
            return ResponseEntity.ok(statusInfo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = BY_ID_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "status1", summary = "Update status using ID.",
            description = "Update a driver's status using his ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated status.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LocationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<List<LocationResponseDto>> updateDriver(
            @PathVariable @Valid Long driverId,
            @RequestBody @Valid LiveStatusRequestDto liveStatusRequestDto) {
        liveStatusService.updateDriverStatus(driverId, liveStatusRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
        /*
         * Bad Request: caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }
}