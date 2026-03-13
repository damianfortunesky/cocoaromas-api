package com.cocoaromas.api.application.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.cocoaromas.api.application.port.out.admin.ManageAdminOrdersPort;
import com.cocoaromas.api.application.port.out.admin.OrderPaidHookPort;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetailItem;
import com.cocoaromas.api.domain.admin.order.UpdateOrderStatusCommand;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminOrdersServiceTest {

    @Mock
    private ManageAdminOrdersPort manageAdminOrdersPort;

    @Mock
    private OrderPaidHookPort orderPaidHookPort;

    private AdminOrdersService service;

    @BeforeEach
    void setUp() {
        service = new AdminOrdersService(manageAdminOrdersPort, orderPaidHookPort);
    }

    @Test
    void shouldUpdateToPaidAndTriggerHook() {
        given(manageAdminOrdersPort.findById(10L)).willReturn(Optional.of(detail(OrderStatus.ESPERANDO_PAGO)));
        given(manageAdminOrdersPort.updateStatus(10L, OrderStatus.PAGADO, "transfer ok"))
                .willReturn(detail(OrderStatus.PAGADO));

        AdminOrderDetail updated = service.updateStatus(10L, new UpdateOrderStatusCommand(OrderStatus.PAGADO, "transfer ok"));

        assertThat(updated.status()).isEqualTo(OrderStatus.PAGADO);
        verify(orderPaidHookPort).onOrderPaid(updated, "transfer ok");
    }

    @Test
    void shouldRejectInvalidTransition() {
        given(manageAdminOrdersPort.findById(10L)).willReturn(Optional.of(detail(OrderStatus.PENDIENTE)));

        assertThatThrownBy(() -> service.updateStatus(10L, new UpdateOrderStatusCommand(OrderStatus.ENVIADO, null)))
                .isInstanceOf(AdminOrderValidationException.class)
                .hasMessageContaining("Transición no permitida");

        verify(manageAdminOrdersPort, never()).updateStatus(10L, OrderStatus.ENVIADO, null);
    }

    @Test
    void shouldRejectSameStatus() {
        given(manageAdminOrdersPort.findById(10L)).willReturn(Optional.of(detail(OrderStatus.PAGADO)));

        assertThatThrownBy(() -> service.updateStatus(10L, new UpdateOrderStatusCommand(OrderStatus.PAGADO, null)))
                .isInstanceOf(AdminOrderValidationException.class)
                .hasMessageContaining("ya se encuentra");
    }

    private AdminOrderDetail detail(OrderStatus status) {
        return new AdminOrderDetail(
                10L,
                OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                null,
                status,
                PaymentMethod.TRANSFER,
                3L,
                "Cliente",
                "cliente@correo.com",
                new BigDecimal("100.00"),
                BigDecimal.ZERO,
                new BigDecimal("100.00"),
                "nota",
                List.of(new AdminOrderDetailItem(1L, "Prod", new BigDecimal("100.00"), 1, new BigDecimal("100.00"), null))
        );
    }
}
