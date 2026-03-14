package com.cocoaromas.api.application.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cocoaromas.api.application.port.out.order.LoadProductsForOrderPort;
import com.cocoaromas.api.application.port.out.order.SaveOrderPort;
import com.cocoaromas.api.domain.order.CreateOrderCommand;
import com.cocoaromas.api.domain.order.CreateOrderItem;
import com.cocoaromas.api.domain.order.CreatedOrder;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    private LoadProductsForOrderPort loadProductsForOrderPort;

    @Mock
    private SaveOrderPort saveOrderPort;

    @Captor
    private ArgumentCaptor<SaveOrderPort.OrderToSave> orderCaptor;

    private CreateOrderService createOrderService;

    @BeforeEach
    void setUp() {
        createOrderService = new CreateOrderService(loadProductsForOrderPort, saveOrderPort, new ObjectMapper());
    }

    @Test
    void shouldResolveTransferAsEsperandoPagoAndCalculateTotal() {
        given(loadProductsForOrderPort.findById(1L)).willReturn(java.util.Optional.of(
                new LoadProductsForOrderPort.ProductForOrder(1L, "Sahumerio", new BigDecimal("3500.00"), 10, false, null, true)
        ));
        given(saveOrderPort.save(orderCaptor.capture())).willReturn(new CreatedOrder(
                90L,
                OrderStatus.ESPERANDO_PAGO,
                new BigDecimal("7000.00"),
                OffsetDateTime.now(),
                PaymentMethod.TRANSFER,
                List.of()
        ));

        CreatedOrder created = createOrderService.createOrder(new CreateOrderCommand(
                5L,
                List.of(new CreateOrderItem(1L, 2, null)),
                PaymentMethod.TRANSFER,
                null,
                null
        ));

        assertThat(created.status()).isEqualTo(OrderStatus.ESPERANDO_PAGO);
        assertThat(orderCaptor.getValue().total()).isEqualByComparingTo("7000.00");
    }

    @Test
    void shouldFailWhenItemListIsEmpty() {
        assertThatThrownBy(() -> createOrderService.createOrder(new CreateOrderCommand(1L, List.of(), PaymentMethod.TRANSFER, null, null)))
                .isInstanceOf(OrderValidationException.class);
    }

    @Test
    void shouldLoadProductOnlyOnceWhenSameProductHasMultipleVariants() {
        given(loadProductsForOrderPort.findById(1L)).willReturn(java.util.Optional.of(
                new LoadProductsForOrderPort.ProductForOrder(
                        1L,
                        "Sahumerio",
                        new BigDecimal("3500.00"),
                        10,
                        true,
                        "[{\"id\":\"v1\",\"available\":true},{\"id\":\"v2\",\"available\":true}]",
                        true)
        ));
        given(saveOrderPort.save(orderCaptor.capture())).willReturn(new CreatedOrder(
                90L,
                OrderStatus.PENDIENTE,
                new BigDecimal("7000.00"),
                OffsetDateTime.now(),
                PaymentMethod.MERCADO_PAGO,
                List.of()
        ));

        createOrderService.createOrder(new CreateOrderCommand(
                5L,
                List.of(
                        new CreateOrderItem(1L, 1, "v1"),
                        new CreateOrderItem(1L, 1, "v2")
                ),
                PaymentMethod.MERCADO_PAGO,
                null,
                null
        ));

        verify(loadProductsForOrderPort, times(1)).findById(1L);
    }
}
