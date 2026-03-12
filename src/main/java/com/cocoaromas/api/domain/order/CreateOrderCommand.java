package com.cocoaromas.api.domain.order;

import java.util.List;

public record CreateOrderCommand(
        Long userId,
        List<CreateOrderItem> items,
        PaymentMethod paymentMethod,
        String notes,
        String deliveryMethod
) {
}
