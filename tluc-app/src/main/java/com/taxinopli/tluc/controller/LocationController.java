package com.taxinopli.tluc.controller;

import com.taxinopli.tluc.constant.ResponseStatusHttp;
import com.taxinopli.tluc.dto.DriversInPolyRequestDto;
import com.taxinopli.tluc.dto.LocationResponseDto;
import com.taxinopli.tluc.dto.NearbyDriversByPointRequestDto;
import com.taxinopli.tluc.service.LocationService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = LocationController.LOCATION_MAPPING, produces = APPLICATION_JSON_VALUE)
@Tag(name = "3. Location", description = "All needed endpoints for location operations.")
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    static final String LOCATION_MAPPING = "/location";
    private static final String BY_POINT_MAPPING = "/point";
    private static final String IN_POLY_MAPPING = "/poly";

    @Autowired
    private final LocationService locationService;

    @PostMapping(path = BY_POINT_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "location1", summary = "Get nearby drivers from point.",
            description = "Returns a list of nearby drivers locations around coordinates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of locations.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LocationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<List<LocationResponseDto>> getNearbyDriversByPoint(
            @RequestBody @Valid NearbyDriversByPointRequestDto nearbyDriversByPointRequestDto) {
        List<LocationResponseDto> locationResponseDtos = locationService.getNearbyDriversByPoint(
                nearbyDriversByPointRequestDto);
        return new ResponseEntity<>(locationResponseDtos, HttpStatus.OK);
        /*
         * Bad Request: caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }

    @PostMapping(path = IN_POLY_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "location2", summary = "Get drivers in poly.",
            description = "Returns a list of drivers locations in a polygon.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of locations.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LocationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<List<LocationResponseDto>> getDriversInPolygon(
            @RequestBody @Valid DriversInPolyRequestDto driversInPolyRequestDto) {
        List<LocationResponseDto> locationResponseDtos = locationService.getDriversInPolygon(
                driversInPolyRequestDto);
        return new ResponseEntity<>(locationResponseDtos, HttpStatus.OK);
        /*
         * Bad Request:
         *      - polygon not valid (last point should be equal to first point).
         *      - caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }
}