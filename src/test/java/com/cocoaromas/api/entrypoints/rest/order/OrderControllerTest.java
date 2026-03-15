package com.cocoaromas.api.entrypoints.rest.order;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.order.CreateOrderUseCase;
import com.cocoaromas.api.application.port.in.order.GetMyOrderDetailUseCase;
import com.cocoaromas.api.application.port.in.order.GetMyOrdersUseCase;
import com.cocoaromas.api.application.port.out.auth.SecurityContextPort;
import com.cocoaromas.api.domain.order.CreatedOrder;
import com.cocoaromas.api.domain.order.CreatedOrderItem;
import com.cocoaromas.api.domain.order.CustomerOrderDetail;
import com.cocoaromas.api.domain.order.CustomerOrderItemDetail;
import com.cocoaromas.api.domain.order.CustomerOrderPage;
import com.cocoaromas.api.domain.order.CustomerOrderSummary;
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

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @MockBean
    private GetMyOrdersUseCase getMyOrdersUseCase;

    @MockBean
    private GetMyOrderDetailUseCase getMyOrderDetailUseCase;

    @MockBean
    private SecurityContextPort securityContextPort;

    @Test
    void shouldCreateOrder() throws Exception {
        given(securityContextPort.getAuthenticatedUserId()).willReturn(1L);
        given(createOrderUseCase.createOrder(org.mockito.ArgumentMatchers.any())).willReturn(new CreatedOrder(
                15L,
                OrderStatus.ESPERANDO_PAGO,
                new BigDecimal("23500.00"),
                OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                PaymentMethod.TRANSFER,
                List.of(new CreatedOrderItem(1L, "Sahumerio", null, 2, new BigDecimal("3500.00"), new BigDecimal("7000.00")))
        ));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    { "productId": 1, "quantity": 2 }
                                  ],
                                  "paymentMethod": "TRANSFER",
                                  "notes": "Entregar por la tarde"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(15))
                .andExpect(jsonPath("$.status").value("esperando_pago"))
                .andExpect(jsonPath("$.paymentMethod").value("TRANSFER"))
                .andExpect(jsonPath("$.items[0].unitPrice").value(3500.00));
    }

    @Test
    void shouldListMyOrders() throws Exception {
        given(securityContextPort.getAuthenticatedUserId()).willReturn(1L);
        given(getMyOrdersUseCase.getMyOrders(1L, 0, 10)).willReturn(new CustomerOrderPage(
                List.of(new CustomerOrderSummary(
                        15L,
                        OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                        OrderStatus.ESPERANDO_PAGO,
                        new BigDecimal("23500.00"),
                        PaymentMethod.TRANSFER,
                        3
                )),
                0,
                10,
                1,
                1
        ));

        mockMvc.perform(get("/api/v1/orders/me?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].orderId").value(15))
                .andExpect(jsonPath("$.items[0].status").value("esperando_pago"))
                .andExpect(jsonPath("$.items[0].quantityOfItems").value(3));
    }

    @Test
    void shouldGetMyOrderDetail() throws Exception {
        given(securityContextPort.getAuthenticatedUserId()).willReturn(1L);
        given(getMyOrderDetailUseCase.getMyOrderDetail(1L, 15L)).willReturn(new CustomerOrderDetail(
                15L,
                1L,
                OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                OrderStatus.ESPERANDO_PAGO,
                PaymentMethod.TRANSFER,
                new BigDecimal("23500.00"),
                new BigDecimal("25000.00"),
                new BigDecimal("1500.00"),
                "Entregar por la tarde",
                List.of(new CustomerOrderItemDetail(
                        1L,
                        "Sahumerio",
                        new BigDecimal("3500.00"),
                        2,
                        new BigDecimal("7000.00"),
                        "https://cdn.example.com/sahumerio.jpg"
                ))
        ));

        mockMvc.perform(get("/api/v1/orders/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(15))
                .andExpect(jsonPath("$.items[0].productId").value(1))
                .andExpect(jsonPath("$.items[0].imageUrl").value("https://cdn.example.com/sahumerio.jpg"));
    }
}
