package com.cocoaromas.api.domain.admin.order;

import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record AdminOrderDetail(
        Long orderId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OrderStatus status,
        PaymentMethod paymentMethod,
        Long customerId,
        String customerName,
        String customerEmail,
        BigDecimal subtotal,
        BigDecimal discounts,
        BigDecimal total,
        String notes,
        List<AdminOrderDetailItem> items
) {
}
