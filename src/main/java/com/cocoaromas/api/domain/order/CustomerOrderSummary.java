package com.cocoaromas.api.domain.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CustomerOrderSummary(
        Long orderId,
        OffsetDateTime createdAt,
        OrderStatus status,
        BigDecimal total,
        PaymentMethod paymentMethod,
        Integer quantityOfItems
) {
}
