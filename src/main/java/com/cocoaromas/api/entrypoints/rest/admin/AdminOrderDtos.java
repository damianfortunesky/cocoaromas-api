package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetailItem;
import com.cocoaromas.api.domain.admin.order.AdminOrderItem;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class AdminOrderDtos {

    private AdminOrderDtos() {
    }

    @Schema(name = "AdminOrdersPageResponse")
    public record AdminOrdersPageResponse(
            List<AdminOrderSummaryResponse> items,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        static AdminOrdersPageResponse fromDomain(com.cocoaromas.api.domain.admin.order.AdminOrderPage page) {
            return new AdminOrdersPageResponse(
                    page.items().stream().map(AdminOrderSummaryResponse::fromDomain).toList(),
                    page.page(),
                    page.size(),
                    page.totalElements(),
                    page.totalPages()
            );
        }
    }

    @Schema(name = "AdminOrderSummaryResponse")
    public record AdminOrderSummaryResponse(
            Long orderId,
            OffsetDateTime createdAt,
            OrderStatus status,
            PaymentMethod paymentMethod,
            Long customerId,
            String customerName,
            String customerEmail,
            BigDecimal total,
            int quantityOfItems
    ) {
        static AdminOrderSummaryResponse fromDomain(AdminOrderItem item) {
            return new AdminOrderSummaryResponse(
                    item.orderId(),
                    item.createdAt(),
                    item.status(),
                    item.paymentMethod(),
                    item.customerId(),
                    item.customerName(),
                    item.customerEmail(),
                    item.total(),
                    item.quantityOfItems()
            );
        }
    }

    @Schema(name = "AdminOrderDetailResponse")
    public record AdminOrderDetailResponse(
            Long orderId,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            OrderStatus status,
            PaymentMethod paymentMethod,
            AdminOrderCustomerResponse customer,
            BigDecimal subtotal,
            BigDecimal discounts,
            BigDecimal total,
            String notes,
            List<AdminOrderItemDetailResponse> items
    ) {
        static AdminOrderDetailResponse fromDomain(AdminOrderDetail detail) {
            return new AdminOrderDetailResponse(
                    detail.orderId(),
                    detail.createdAt(),
                    detail.updatedAt(),
                    detail.status(),
                    detail.paymentMethod(),
                    new AdminOrderCustomerResponse(detail.customerId(), detail.customerName(), detail.customerEmail()),
                    detail.subtotal(),
                    detail.discounts(),
                    detail.total(),
                    detail.notes(),
                    detail.items().stream().map(AdminOrderItemDetailResponse::fromDomain).toList()
            );
        }
    }

    @Schema(name = "AdminOrderCustomerResponse")
    public record AdminOrderCustomerResponse(
            Long id,
            String name,
            String email
    ) {
    }

    @Schema(name = "AdminOrderItemDetailResponse")
    public record AdminOrderItemDetailResponse(
            Long productId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal subtotal,
            String mainImageUrl
    ) {
        static AdminOrderItemDetailResponse fromDomain(AdminOrderDetailItem item) {
            return new AdminOrderItemDetailResponse(
                    item.productId(),
                    item.productName(),
                    item.unitPrice(),
                    item.quantity(),
                    item.subtotal(),
                    item.mainImageUrl()
            );
        }
    }

    @Schema(name = "AdminOrderStatusUpdateRequest", description = "Actualiza estado del pedido según transiciones válidas")
    public record AdminOrderStatusUpdateRequest(
            @NotNull OrderStatus newStatus,
            String reason
    ) {
    }
}
