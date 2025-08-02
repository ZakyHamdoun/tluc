package com.taxinopli.tluc.controller;

import com.taxinopli.tluc.constant.ResponseStatusHttp;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = RideController.RIDE_MAPPING, produces = APPLICATION_JSON_VALUE)
@Tag(name = "3. Ride", description = "All needed endpoints for ride operations.")
@RequiredArgsConstructor
@Slf4j
public class RideController {
    static final String RIDE_MAPPING = "/ride";
    private static final String BY_ID_MAPPING = "/";

    @Autowired
    private final RideService rideService;

    @GetMapping(path = BY_ID_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "ride1", summary = "Get nearby all locations of a ride.",
            description = "Returns a list of all rides locations (list of lists).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides locations.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LocationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<Map<Long, List<LocationResponseDto>>> getRidesLocations(
            @RequestParam(value = "rideIds") @Valid List<Long> rideIds) {
        Map<Long, List<LocationResponseDto>> rides = rideService.getRidesLocations(rideIds);
        return new ResponseEntity<>(rides, HttpStatus.OK);
        /*
         * Bad Request: caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }
}