package com.cocoaromas.api.domain.order;

import java.math.BigDecimal;

public record CustomerOrderItemDetail(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal,
        String imageUrl
) {
}
