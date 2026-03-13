package com.cocoaromas.api.domain.admin.order;

import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.time.OffsetDateTime;

public record AdminOrderQuery(
        String search,
        OrderStatus status,
        PaymentMethod paymentMethod,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo,
        int page,
        int size,
        String sortBy,
        String direction
) {
}
