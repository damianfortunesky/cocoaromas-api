package com.cocoaromas.api.application.port.out.admin;

import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderPage;
import com.cocoaromas.api.domain.admin.order.AdminOrderQuery;
import com.cocoaromas.api.domain.order.OrderStatus;
import java.util.Optional;

public interface ManageAdminOrdersPort {

    AdminOrderPage find(AdminOrderQuery query);

    Optional<AdminOrderDetail> findById(Long orderId);

    AdminOrderDetail updateStatus(Long orderId, OrderStatus newStatus, String reason);
}
