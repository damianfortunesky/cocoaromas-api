package com.cocoaromas.api.application.service.order;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(Long productId) {
        super("Producto no encontrado: " + productId);
    }
}
