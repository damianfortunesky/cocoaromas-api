package com.cocoaromas.api.application.port.in.order;

import com.cocoaromas.api.domain.order.CustomerOrderPage;

public interface GetMyOrdersUseCase {
    CustomerOrderPage getMyOrders(Long userId, int page, int size);
}
