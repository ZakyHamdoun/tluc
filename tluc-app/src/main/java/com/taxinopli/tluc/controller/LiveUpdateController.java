package com.taxinopli.tluc.controller;

import com.taxinopli.tluc.constant.ResponseStatusHttp;
import com.taxinopli.tluc.service.LiveUpdateProducerService;
import com.taxinopli.tluc.service.LiveUpdateService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.taxinopli.tluc.dto.LiveUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = LiveUpdateController.UPDATE_MAPPING)
@Tag(name = "1. Live Update", description = "All needed endpoints for live updates operations.")
@RequiredArgsConstructor
@Slf4j
public class LiveUpdateController {
    static final String UPDATE_MAPPING = "/update";
    private static final String DRIVER_UPDATE = "/driver-update";
    private static final String FORCE_DRIVER_UPDATE = "/force-driver-update";

    @Autowired
    private final LiveUpdateProducerService liveUpdateProducerService;

    @Autowired
    private final LiveUpdateService liveUpdateService;

    @MessageMapping(DRIVER_UPDATE)
    @Operation(operationId = "update0", summary = "Update a taxi's information.",
            description = "Real-time taxi location and status update.")
    public void driverUpdate(@RequestBody @Valid LiveUpdateRequestDto liveUpdateRequestDto){
        log.info("WebSocket: Received taxi location update for driver: {}", liveUpdateRequestDto.getDriverId());
        liveUpdateProducerService.produceLiveUpdate(liveUpdateRequestDto);
        log.info("WebSocket: Taxi location update sent to Kafka for driver: {}",
                liveUpdateRequestDto.getDriverId());
    }

    @PostMapping(path = FORCE_DRIVER_UPDATE, produces = APPLICATION_JSON_VALUE)
    @Operation(operationId = "update1", summary = "Force update a taxi's information manually.",
            description = "Manual taxi location and status force update.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated status.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = ResponseStatusHttp.HTTP_BAD_REQUEST,
                    content = @Content),
            @ApiResponse(responseCode = "401", description = ResponseStatusHttp.HTTP_UNAUTHORIZED,
                    content = @Content)
    })
    public void driverForceUpdate(@RequestBody @Valid LiveUpdateRequestDto liveUpdateRequestDto){
        liveUpdateService.driverForceUpdate(liveUpdateRequestDto);
        /*
         * Bad Request: caught at input validation by annotations.
         * Unauthorized: caught at token validation.
         */
    }
}
