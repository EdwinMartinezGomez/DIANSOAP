package com.dian.soap.DianSoap.controller;

import java.time.OffsetDateTime;

public class ErrorResponse {

    private final String message;
    private final OffsetDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
