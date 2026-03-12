package com.cocoaromas.api.domain.order;

public record CreateOrderItem(Long productId, Integer quantity, String variantId) {
}
