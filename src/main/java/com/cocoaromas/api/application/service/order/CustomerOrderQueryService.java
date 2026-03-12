package com.cocoaromas.api.application.service.order;

import com.cocoaromas.api.application.port.in.order.GetMyOrderDetailUseCase;
import com.cocoaromas.api.application.port.in.order.GetMyOrdersUseCase;
import com.cocoaromas.api.application.port.out.order.LoadCustomerOrdersPort;
import com.cocoaromas.api.domain.order.CustomerOrderDetail;
import com.cocoaromas.api.domain.order.CustomerOrderPage;
import org.springframework.stereotype.Service;

@Service
public class CustomerOrderQueryService implements GetMyOrdersUseCase, GetMyOrderDetailUseCase {

    private final LoadCustomerOrdersPort loadCustomerOrdersPort;

    public CustomerOrderQueryService(LoadCustomerOrdersPort loadCustomerOrdersPort) {
        this.loadCustomerOrdersPort = loadCustomerOrdersPort;
    }

    @Override
    public CustomerOrderPage getMyOrders(Long userId, int page, int size) {
        if (userId == null) {
            throw new InvalidOrderException("Usuario no autenticado");
        }

        int normalizedPage = Math.max(page, 0);
        int normalizedSize = Math.min(Math.max(size, 1), 100);
        return loadCustomerOrdersPort.findByUserId(userId, normalizedPage, normalizedSize);
    }

    @Override
    public CustomerOrderDetail getMyOrderDetail(Long userId, Long orderId) {
        if (userId == null) {
            throw new InvalidOrderException("Usuario no autenticado");
        }

        CustomerOrderDetail detail = loadCustomerOrdersPort.findDetailById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!userId.equals(detail.userId())) {
            throw new OrderOwnershipException(orderId);
        }

        return detail;
    }
}
