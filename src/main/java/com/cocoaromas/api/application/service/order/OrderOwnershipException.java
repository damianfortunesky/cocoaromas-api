package com.cocoaromas.api.application.service.order;

public class OrderOwnershipException extends RuntimeException {

    public OrderOwnershipException(Long orderId) {
        super("El pedido " + orderId + " no pertenece al usuario autenticado");
    }
}
