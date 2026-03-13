package com.cocoaromas.api.domain.admin.order;

import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AdminOrderItem(
        Long orderId,
        OffsetDateTime createdAt,
        OrderStatus status,
        PaymentMethod paymentMethod,
        Long customerId,
        String customerName,
        String customerEmail,
        BigDecimal total,
        int quantityOfItems
) {
}
