package com.taxinopli.tluc.controller;

import com.taxinopli.tluc.constant.ResponseStatusHttp;
import com.taxinopli.tluc.dto.LiveDriverRequestDto;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.service.LiveDriverService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = LiveDriverController.DRIVER_MAPPING, produces = APPLICATION_JSON_VALUE)
@Tag(name = "2. Driver", description = "All needed endpoints for driver's information updates.")
@RequiredArgsConstructor
public class LiveDriverController {
    static final String DRIVER_MAPPING = "/driver";
    private static final String BY_ID_MAPPING = "/{driverId}";

    @Autowired
    private final LiveDriverService liveDriverService;

    @PutMapping(path = BY_ID_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "driver1", summary = "Update driver information using ID.",
            description = "Update a driver's information using his ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated information.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LocationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<List<LocationResponseDto>> updateDriver(
            @PathVariable @Valid Long driverId,
            @RequestBody @Valid LiveDriverRequestDto liveDriverRequestDto) {
        liveDriverService.updateLiveDriver(driverId, liveDriverRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
        /*
         * Bad Request: caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }
}