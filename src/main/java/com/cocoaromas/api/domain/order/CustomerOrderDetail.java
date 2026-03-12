package com.cocoaromas.api.domain.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record CustomerOrderDetail(
        Long orderId,
        Long userId,
        OffsetDateTime createdAt,
        OrderStatus status,
        PaymentMethod paymentMethod,
        BigDecimal total,
        BigDecimal subtotal,
        BigDecimal discounts,
        String notes,
        List<CustomerOrderItemDetail> items
) {
}
