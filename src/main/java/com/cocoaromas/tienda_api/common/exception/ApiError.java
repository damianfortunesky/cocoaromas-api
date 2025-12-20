package com.cocoaromas.tienda_api.common.exception;

import java.time.Instant;

public class ApiError {
    public int status;
    public String error;
    public String message;
    public Instant timestamp;

    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }
}
