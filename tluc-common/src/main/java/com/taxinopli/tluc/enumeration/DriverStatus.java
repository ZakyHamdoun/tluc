package com.taxinopli.tluc.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Driver's current status")
public enum DriverStatus {
    OCCUPIED,
    FREE,
    APPROACHING;

    @JsonCreator
    public static DriverStatus fromString(String key) {
        return key == null ? null : DriverStatus.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

