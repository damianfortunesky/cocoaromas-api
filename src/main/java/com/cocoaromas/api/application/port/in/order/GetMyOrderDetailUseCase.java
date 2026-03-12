package com.cocoaromas.api.application.port.in.order;

import com.cocoaromas.api.domain.order.CustomerOrderDetail;

public interface GetMyOrderDetailUseCase {
    CustomerOrderDetail getMyOrderDetail(Long userId, Long orderId);
}

