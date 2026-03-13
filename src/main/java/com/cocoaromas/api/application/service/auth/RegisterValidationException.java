package com.cocoaromas.api.application.service.auth;

public class RegisterValidationException extends RuntimeException {

    public RegisterValidationException(String message) {
        super(message);
    }
}
