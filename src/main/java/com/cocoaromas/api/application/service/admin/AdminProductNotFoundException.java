package com.cocoaromas.api.application.service.admin;

public class AdminProductNotFoundException extends RuntimeException {
    public AdminProductNotFoundException(Long id) {
        super("Producto no encontrado: " + id);
    }
}
