package com.cocoaromas.api.application.service.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Pedido no encontrado: " + orderId);
    }
}
