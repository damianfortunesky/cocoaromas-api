package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class AdminProductDtos {

    private AdminProductDtos() {
    }

    @Schema(name = "AdminProductPageResponse")
    public record ProductsPageResponse(List<AdminProductListItemResponse> items, int page, int size, long totalElements, int totalPages) {
        static ProductsPageResponse fromDomain(AdminProductPage page) {
            return new ProductsPageResponse(page.items().stream().map(AdminProductListItemResponse::fromDomain).toList(),
                    page.page(), page.size(), page.totalElements(), page.totalPages());
        }
    }

    @Schema(name = "AdminProductListItemResponse")
    public record AdminProductListItemResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            CategoryResponse category,
            Integer stockQuantity,
            String imageUrl,
            boolean isActive,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        static AdminProductListItemResponse fromDomain(AdminProduct product) {
            return new AdminProductListItemResponse(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    new CategoryResponse(product.categoryId(), product.categoryName()),
                    product.stockQuantity(),
                    product.imageUrl(),
                    product.isActive(),
                    product.createdAt(),
                    product.updatedAt()
            );
        }
    }

    @Schema(name = "AdminProductDetailResponse")
    public record AdminProductDetailResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            CategoryResponse category,
            Integer stockQuantity,
            String imageUrl,
            boolean isActive,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        static AdminProductDetailResponse fromDomain(AdminProduct product) {
            return new AdminProductDetailResponse(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    new CategoryResponse(product.categoryId(), product.categoryName()),
                    product.stockQuantity(),
                    product.imageUrl(),
                    product.isActive(),
                    product.createdAt(),
                    product.updatedAt()
            );
        }
    }

    @Schema(name = "AdminCategoryResponse")
    public record CategoryResponse(Long id, String name) {
    }

    @Schema(name = "AdminUpsertProductRequest")
    public record UpsertProductRequest(
            @NotBlank String name,
            @NotBlank String description,
            @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal price,
            @NotNull Long categoryId,
            @NotNull Integer stockQuantity,
            String imageUrl,
            Boolean isActive
    ) {
    }

    @Schema(name = "AdminUpdateProductStatusRequest")
    public record UpdateStatusRequest(@NotNull Boolean active) {
    }

    @Schema(name = "AdminProductErrorResponse")
    public record ErrorResponse(String code, String message) {
    }
}
