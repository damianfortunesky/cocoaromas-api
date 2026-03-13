package com.cocoaromas.api.application.service.auth;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(String email) {
        super("El email ya está registrado: " + email);
    }
}
