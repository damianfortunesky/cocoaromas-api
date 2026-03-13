package com.cocoaromas.api.application.port.in.admin;

import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderPage;
import com.cocoaromas.api.domain.admin.order.AdminOrderQuery;
import com.cocoaromas.api.domain.admin.order.UpdateOrderStatusCommand;

public interface AdminOrdersUseCase {
    AdminOrderPage list(AdminOrderQuery query);

    AdminOrderDetail getById(Long orderId);

    AdminOrderDetail updateStatus(Long orderId, UpdateOrderStatusCommand command);
}
