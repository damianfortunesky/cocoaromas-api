package com.cocoaromas.api.application.port.out.order;

import com.cocoaromas.api.domain.order.CustomerOrderDetail;
import com.cocoaromas.api.domain.order.CustomerOrderPage;
import java.util.Optional;

public interface LoadCustomerOrdersPort {
    CustomerOrderPage findByUserId(Long userId, int page, int size);

    Optional<CustomerOrderDetail> findDetailById(Long orderId);
}
