package com.taxinopli.tluc.controller;

import com.taxinopli.tluc.constant.ResponseStatusHttp;
import com.taxinopli.tluc.dto.*;
import com.taxinopli.tluc.service.DurationService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = DurationController.DURATION_MAPPING, produces = APPLICATION_JSON_VALUE)
@Tag(name = "4. Duration", description = "All needed operations for duration operations.")
@RequiredArgsConstructor
@Slf4j
public class DurationController {
    static final String DURATION_MAPPING = "/duration";
    private static final String BY_POINT_MAPPING = "/point";
    private static final String IN_POLY_MAPPING = "/poly";

    @Autowired
    private final DurationService durationService;

    @PostMapping(path = BY_POINT_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "duration1", summary = "Get nearby drivers from point by duration.",
            description = "Returns a list of nearby drivers durations around coordinates for a minimum duration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of durations.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DurationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<List<DurationResponseDto>> getNearbyDriversByPoint(
            @RequestBody @Valid NearbyDriversByPointDurationRequestDto nearbyDriversByPointDurationRequestDto) {
        List<DurationResponseDto> durationResponseDtos = durationService.getNearbyDriversByPointDuration(
                nearbyDriversByPointDurationRequestDto);
        return new ResponseEntity<>(durationResponseDtos, HttpStatus.OK);
        /*
         * Bad Request: caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }

    @PostMapping(path = IN_POLY_MAPPING, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "duration2", summary = "Get drivers in poly by duration.",
            description = "Returns a list of drivers durations in a polygon for a minimum duration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of durations.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DurationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public ResponseEntity<List<DurationResponseDto>> getDriversInPolygonDuration(
            @RequestBody @Valid DriversInPolyDurationRequestDto driversInPolyDurationRequestDto) {
        List<DurationResponseDto> durationResponseDtos = durationService.getDriversInPolygonDuration(
                driversInPolyDurationRequestDto);
        return new ResponseEntity<>(durationResponseDtos, HttpStatus.OK);
        /*
         * Bad Request:
         *      - polygon not valid (last point should be equal to first point).
         *      - caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }
}