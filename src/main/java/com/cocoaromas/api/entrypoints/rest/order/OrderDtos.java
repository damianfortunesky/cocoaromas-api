package com.cocoaromas.api.entrypoints.rest.order;

import com.cocoaromas.api.domain.order.CreateOrderCommand;
import com.cocoaromas.api.domain.order.CreateOrderItem;
import com.cocoaromas.api.domain.order.CreatedOrder;
import com.cocoaromas.api.domain.order.CreatedOrderItem;
import com.cocoaromas.api.domain.order.CustomerOrderDetail;
import com.cocoaromas.api.domain.order.CustomerOrderItemDetail;
import com.cocoaromas.api.domain.order.CustomerOrderPage;
import com.cocoaromas.api.domain.order.CustomerOrderSummary;
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

    @Schema(name = "MyOrdersPageResponse")
    public record MyOrdersPageResponse(
            List<MyOrderSummaryResponse> items,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        public static MyOrdersPageResponse fromDomain(CustomerOrderPage page) {
            return new MyOrdersPageResponse(
                    page.items().stream().map(MyOrderSummaryResponse::fromDomain).toList(),
                    page.page(),
                    page.size(),
                    page.totalElements(),
                    page.totalPages()
            );
        }
    }

    @Schema(name = "MyOrderSummaryResponse")
    public record MyOrderSummaryResponse(
            Long orderId,
            OffsetDateTime createdAt,
            String status,
            BigDecimal total,
            String paymentMethod,
            Integer quantityOfItems
    ) {
        static MyOrderSummaryResponse fromDomain(CustomerOrderSummary order) {
            return new MyOrderSummaryResponse(
                    order.orderId(),
                    order.createdAt(),
                    order.status().name().toLowerCase(),
                    order.total(),
                    order.paymentMethod().name(),
                    order.quantityOfItems()
            );
        }
    }

    @Schema(name = "MyOrderDetailResponse")
    public record MyOrderDetailResponse(
            Long orderId,
            OffsetDateTime createdAt,
            String status,
            String paymentMethod,
            BigDecimal total,
            BigDecimal subtotal,
            BigDecimal discounts,
            String notes,
            List<MyOrderItemDetailResponse> items
    ) {
        public static MyOrderDetailResponse fromDomain(CustomerOrderDetail order) {
            return new MyOrderDetailResponse(
                    order.orderId(),
                    order.createdAt(),
                    order.status().name().toLowerCase(),
                    order.paymentMethod().name(),
                    order.total(),
                    order.subtotal(),
                    order.discounts(),
                    order.notes(),
                    order.items().stream().map(MyOrderItemDetailResponse::fromDomain).toList()
            );
        }
    }

    @Schema(name = "MyOrderItemDetailResponse")
    public record MyOrderItemDetailResponse(
            Long productId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal subtotal,
            String imageUrl
    ) {
        static MyOrderItemDetailResponse fromDomain(CustomerOrderItemDetail item) {
            return new MyOrderItemDetailResponse(
                    item.productId(),
                    item.productName(),
                    item.unitPrice(),
                    item.quantity(),
                    item.subtotal(),
                    item.imageUrl()
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
