package com.cocoaromas.api.application.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.cocoaromas.api.application.port.out.order.LoadCustomerOrdersPort;
import com.cocoaromas.api.domain.order.CustomerOrderDetail;
import com.cocoaromas.api.domain.order.CustomerOrderPage;
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
class CustomerOrderQueryServiceTest {

    @Mock
    private LoadCustomerOrdersPort loadCustomerOrdersPort;

    private CustomerOrderQueryService customerOrderQueryService;

    @BeforeEach
    void setUp() {
        customerOrderQueryService = new CustomerOrderQueryService(loadCustomerOrdersPort);
    }

    @Test
    void shouldNormalizePageAndSize() {
        CustomerOrderPage emptyPage = new CustomerOrderPage(List.of(), 0, 100, 0, 0);
        given(loadCustomerOrdersPort.findByUserId(1L, 0, 100)).willReturn(emptyPage);

        CustomerOrderPage result = customerOrderQueryService.getMyOrders(1L, -1, 999);

        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    void shouldFailWhenOrderDoesNotBelongToUser() {
        given(loadCustomerOrdersPort.findDetailById(10L)).willReturn(Optional.of(new CustomerOrderDetail(
                10L,
                2L,
                OffsetDateTime.now(),
                OrderStatus.PENDIENTE,
                PaymentMethod.MERCADO_PAGO,
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                BigDecimal.ZERO,
                null,
                List.of()
        )));

        assertThatThrownBy(() -> customerOrderQueryService.getMyOrderDetail(1L, 10L))
                .isInstanceOf(OrderOwnershipException.class);
    }

    @Test
    void shouldFailWhenOrderDoesNotExist() {
        given(loadCustomerOrdersPort.findDetailById(55L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> customerOrderQueryService.getMyOrderDetail(1L, 55L))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
