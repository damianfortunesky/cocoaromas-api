package com.cocoaromas.api.application.service.admin;

import com.cocoaromas.api.application.port.in.admin.AdminOrdersUseCase;
import com.cocoaromas.api.application.port.out.admin.ManageAdminOrdersPort;
import com.cocoaromas.api.application.port.out.admin.OrderPaidHookPort;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderPage;
import com.cocoaromas.api.domain.admin.order.AdminOrderQuery;
import com.cocoaromas.api.domain.admin.order.OrderStatusTransitionPolicy;
import com.cocoaromas.api.domain.admin.order.UpdateOrderStatusCommand;
import com.cocoaromas.api.domain.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminOrdersService implements AdminOrdersUseCase {

    private final ManageAdminOrdersPort manageAdminOrdersPort;
    private final OrderPaidHookPort orderPaidHookPort;

    public AdminOrdersService(ManageAdminOrdersPort manageAdminOrdersPort, OrderPaidHookPort orderPaidHookPort) {
        this.manageAdminOrdersPort = manageAdminOrdersPort;
        this.orderPaidHookPort = orderPaidHookPort;
    }

    @Override
    public AdminOrderPage list(AdminOrderQuery query) {
        return manageAdminOrdersPort.find(query);
    }

    @Override
    public AdminOrderDetail getById(Long orderId) {
        return manageAdminOrdersPort.findById(orderId)
                .orElseThrow(() -> new AdminOrderNotFoundException(orderId));
    }

    @Override
    @Transactional
    public AdminOrderDetail updateStatus(Long orderId, UpdateOrderStatusCommand command) {
        if (command.newStatus() == null) {
            throw new AdminOrderValidationException("newStatus es requerido");
        }

        AdminOrderDetail current = getById(orderId);
        if (current.status() == command.newStatus()) {
            throw new AdminOrderValidationException("El pedido ya se encuentra en estado " + command.newStatus());
        }

        if (!OrderStatusTransitionPolicy.isAllowed(current.status(), command.newStatus())) {
            throw new AdminOrderValidationException(
                    "Transición no permitida: " + current.status() + " -> " + command.newStatus()
                            + ". Permitidos: " + OrderStatusTransitionPolicy.allowedTargets(current.status())
            );
        }

        AdminOrderDetail updated = manageAdminOrdersPort.updateStatus(orderId, command.newStatus(), command.reason());
        if (command.newStatus() == OrderStatus.PAGADO) {
            orderPaidHookPort.onOrderPaid(updated, command.reason());
        }
        return updated;
    }
}
