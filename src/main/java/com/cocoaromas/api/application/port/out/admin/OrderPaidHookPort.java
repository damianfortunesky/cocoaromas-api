package com.cocoaromas.api.application.port.out.admin;

import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;

public interface OrderPaidHookPort {
    void onOrderPaid(AdminOrderDetail orderDetail, String reason);
}
