package com.cocoaromas.api.application.port.in.order;

import com.cocoaromas.api.domain.order.CreateOrderCommand;
import com.cocoaromas.api.domain.order.CreatedOrder;

public interface CreateOrderUseCase {
    CreatedOrder createOrder(CreateOrderCommand command);
}
