package com.cocoaromas.api.domain.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record CreatedOrder(
        Long orderId,
        OrderStatus status,
        BigDecimal total,
        OffsetDateTime createdAt,
        PaymentMethod paymentMethod,
        List<CreatedOrderItem> items
) {
}
