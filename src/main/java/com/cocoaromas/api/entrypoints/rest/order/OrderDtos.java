package com.cocoaromas.api.entrypoints.rest.order;

import com.cocoaromas.api.domain.order.CreateOrderCommand;
import com.cocoaromas.api.domain.order.CreateOrderItem;
import com.cocoaromas.api.domain.order.CreatedOrder;
import com.cocoaromas.api.domain.order.CreatedOrderItem;
import com.cocoaromas.api.domain.order.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class OrderDtos {

    private OrderDtos() {
    }

    @Schema(name = "CreateOrderRequest")
    public record CreateOrderRequest(
            @NotEmpty(message = "items es requerido")
            @Valid
            List<CreateOrderItemRequest> items,
            @NotNull(message = "paymentMethod es requerido")
            PaymentMethod paymentMethod,
            String notes,
            String deliveryMethod
    ) {
        public CreateOrderCommand toCommand(Long userId) {
            return new CreateOrderCommand(
                    userId,
                    items.stream().map(item -> new CreateOrderItem(item.productId(), item.quantity(), item.variantId())).toList(),
                    paymentMethod,
                    notes,
                    deliveryMethod
            );
        }
    }

    public record CreateOrderItemRequest(
            @NotNull(message = "productId es requerido") Long productId,
            @NotNull(message = "quantity es requerido") @Min(value = 1, message = "quantity debe ser >= 1") Integer quantity,
            String variantId
    ) {
    }

    @Schema(name = "CreateOrderResponse")
    public record CreateOrderResponse(
            Long orderId,
            String status,
            BigDecimal total,
            OffsetDateTime createdAt,
            String paymentMethod,
            List<OrderItemSummaryResponse> items
    ) {
        public static CreateOrderResponse fromDomain(CreatedOrder order) {
            return new CreateOrderResponse(
                    order.orderId(),
                    order.status().name().toLowerCase(),
                    order.total(),
                    order.createdAt(),
                    order.paymentMethod().name(),
                    order.items().stream().map(OrderItemSummaryResponse::fromDomain).toList()
            );
        }
    }

    public record OrderItemSummaryResponse(
            Long productId,
            String productName,
            String variantId,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {
        public static OrderItemSummaryResponse fromDomain(CreatedOrderItem item) {
            return new OrderItemSummaryResponse(
                    item.productId(),
                    item.productName(),
                    item.variantId(),
                    item.quantity(),
                    item.unitPrice(),
                    item.subtotal()
            );
        }
    }

    public record ErrorResponse(String code, String message) {
    }
}
