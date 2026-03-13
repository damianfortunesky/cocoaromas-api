package com.cocoaromas.api.domain.admin.order;

import com.cocoaromas.api.domain.order.OrderStatus;

public record UpdateOrderStatusCommand(
        OrderStatus newStatus,
        String reason
) {
}
