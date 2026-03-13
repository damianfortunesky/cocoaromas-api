package com.cocoaromas.api.application.service.admin;

public class AdminOrderNotFoundException extends RuntimeException {
    public AdminOrderNotFoundException(Long orderId) {
        super("Pedido no encontrado: " + orderId);
    }
}
