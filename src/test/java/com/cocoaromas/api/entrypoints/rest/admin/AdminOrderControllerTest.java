package com.cocoaromas.api.entrypoints.rest.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.admin.AdminOrdersUseCase;
import com.cocoaromas.api.application.service.admin.AdminOrderValidationException;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetailItem;
import com.cocoaromas.api.domain.admin.order.AdminOrderItem;
import com.cocoaromas.api.domain.admin.order.AdminOrderPage;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminOrdersUseCase adminOrdersUseCase;

    @Test
    void shouldListOrders() throws Exception {
        given(adminOrdersUseCase.list(any())).willReturn(new AdminOrderPage(
                List.of(new AdminOrderItem(
                        10L,
                        OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                        OrderStatus.ESPERANDO_PAGO,
                        PaymentMethod.TRANSFER,
                        2L,
                        "Ana",
                        "ana@mail.com",
                        new BigDecimal("12000.00"),
                        3
                )),
                0, 20, 1, 1
        ));

        mockMvc.perform(get("/api/v1/admin/orders").param("status", "ESPERANDO_PAGO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].orderId").value(10))
                .andExpect(jsonPath("$.items[0].customerEmail").value("ana@mail.com"));
    }

    @Test
    void shouldGetOrderDetail() throws Exception {
        given(adminOrdersUseCase.getById(10L)).willReturn(detail(OrderStatus.PAGADO));

        mockMvc.perform(get("/api/v1/admin/orders/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(10))
                .andExpect(jsonPath("$.customer.email").value("ana@mail.com"));
    }

    @Test
    void shouldPatchOrderStatus() throws Exception {
        given(adminOrdersUseCase.updateStatus(any(), any())).willReturn(detail(OrderStatus.PREPARANDO));

        mockMvc.perform(patch("/api/v1/admin/orders/10/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newStatus": "PREPARANDO",
                                  "reason": "inicia armado"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PREPARANDO"));
    }

    @Test
    void shouldReturn400WhenTransitionIsInvalid() throws Exception {
        given(adminOrdersUseCase.updateStatus(any(), any())).willThrow(new AdminOrderValidationException("Transición no permitida"));

        mockMvc.perform(patch("/api/v1/admin/orders/10/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newStatus": "ENTREGADO"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    private AdminOrderDetail detail(OrderStatus status) {
        return new AdminOrderDetail(
                10L,
                OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                OffsetDateTime.parse("2026-01-02T10:00:00Z"),
                status,
                PaymentMethod.TRANSFER,
                2L,
                "Ana",
                "ana@mail.com",
                new BigDecimal("12000.00"),
                BigDecimal.ZERO,
                new BigDecimal("12000.00"),
                "nota",
                List.of(new AdminOrderDetailItem(1L, "Sahumerio", new BigDecimal("4000.00"), 3, new BigDecimal("12000.00"), "https://img"))
        );
    }
}
