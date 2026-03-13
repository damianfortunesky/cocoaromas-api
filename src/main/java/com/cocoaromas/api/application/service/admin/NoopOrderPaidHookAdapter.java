package com.cocoaromas.api.application.service.admin;

import com.cocoaromas.api.application.port.out.admin.OrderPaidHookPort;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import org.springframework.stereotype.Component;

@Component
public class NoopOrderPaidHookAdapter implements OrderPaidHookPort {

    @Override
    public void onOrderPaid(AdminOrderDetail orderDetail, String reason) {
        // Hook intencionalmente vacío para futura integración de stock y pasarelas de pago.
    }
}
