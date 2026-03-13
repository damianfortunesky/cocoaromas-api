package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.AdminStockItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.OffsetDateTime;
import java.util.List;

public final class AdminStockDtos {

    private AdminStockDtos() {
    }

    @Schema(name = "AdminStockPageResponse")
    public record StockPageResponse(
            List<StockItemResponse> items,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        static StockPageResponse fromDomain(com.cocoaromas.api.domain.admin.stock.AdminStockPage page) {
            return new StockPageResponse(
                    page.items().stream().map(StockItemResponse::fromDomain).toList(),
                    page.page(),
                    page.size(),
                    page.totalElements(),
                    page.totalPages()
            );
        }
    }

    @Schema(name = "AdminStockItemResponse")
    public record StockItemResponse(
            Long productId,
            String productName,
            String category,
            boolean active,
            Integer stockQuantity,
            boolean available,
            boolean lowStock,
            boolean hasVariants,
            String mainImageUrl
    ) {
        static StockItemResponse fromDomain(AdminStockItem item) {
            return new StockItemResponse(
                    item.productId(),
                    item.productName(),
                    item.category(),
                    item.active(),
                    item.stockQuantity(),
                    item.available(),
                    item.lowStock(),
                    item.hasVariants(),
                    item.mainImageUrl()
            );
        }
    }

    @Schema(name = "AdminStockDetailResponse")
    public record StockDetailResponse(
            Long productId,
            String productName,
            String category,
            boolean active,
            Integer stockQuantity,
            boolean available,
            boolean lowStock,
            boolean hasVariants,
            String mainImageUrl,
            OffsetDateTime updatedAt
    ) {
        static StockDetailResponse fromDomain(AdminStockDetail detail) {
            return new StockDetailResponse(
                    detail.productId(),
                    detail.productName(),
                    detail.category(),
                    detail.active(),
                    detail.stockQuantity(),
                    detail.available(),
                    detail.lowStock(),
                    detail.hasVariants(),
                    detail.mainImageUrl(),
                    detail.updatedAt()
            );
        }
    }

    @Schema(name = "AdminStockUpdateRequest", description = "Enviar exactamente uno: newStockQuantity (set absoluto) o adjustment (incremental)")
    public record UpdateStockRequest(
            @PositiveOrZero Integer newStockQuantity,
            Integer adjustment,
            String reason
    ) {
    }
}
