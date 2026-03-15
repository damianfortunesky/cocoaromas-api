package com.cocoaromas.api.application.service.user;

public class MyAddressNotFoundException extends RuntimeException {
    public MyAddressNotFoundException(Long id) { super("Dirección no encontrada: " + id); }
}
