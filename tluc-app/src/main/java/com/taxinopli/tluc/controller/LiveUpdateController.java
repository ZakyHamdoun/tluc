package com.taxinopli.tluc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"Static string in class"}, produces = APPLICATION_JSON_VALUE)
@Tag(name = "1. Live Update", description = "Everything related to live updates.")
@RequiredArgsConstructor
public class LiveUpdateController {
    // TODO
}
