package com.cocoaromas.api.domain.order;

import java.math.BigDecimal;

public record CreatedOrderItem(
        Long productId,
        String productName,
        String variantId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
