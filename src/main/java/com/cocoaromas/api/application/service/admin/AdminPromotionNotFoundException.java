package com.cocoaromas.api.application.service.admin;

public class AdminPromotionNotFoundException extends RuntimeException {
    public AdminPromotionNotFoundException(Long id) {
        super("No se encontró la promoción con id " + id);
    }
}
