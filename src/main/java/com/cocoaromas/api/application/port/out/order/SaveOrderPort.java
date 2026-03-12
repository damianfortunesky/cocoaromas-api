package com.cocoaromas.api.application.port.out.order;

import com.cocoaromas.api.domain.order.CreatedOrder;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.math.BigDecimal;
import java.util.List;

public interface SaveOrderPort {
    CreatedOrder save(OrderToSave order);

    record OrderToSave(
            Long userId,
            PaymentMethod paymentMethod,
            OrderStatus status,
            String notes,
            String deliveryMethod,
            BigDecimal total,
            List<OrderItemToSave> items
    ) {
    }

    record OrderItemToSave(
            Long productId,
            String productName,
            String variantId,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {
    }
}
