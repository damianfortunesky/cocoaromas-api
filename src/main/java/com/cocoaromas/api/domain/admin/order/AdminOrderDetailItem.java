package com.cocoaromas.api.domain.admin.order;

import java.math.BigDecimal;

public record AdminOrderDetailItem(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal,
        String imageUrl
) {
}
