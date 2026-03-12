package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.catalog.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

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
            BigDecimal price,
            CategoryResponse category,
            boolean active,
            String mainImageUrl,
            String stockIndicator,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        static AdminProductListItemResponse fromDomain(AdminProduct product) {
            String stockIndicator = product.stockQuantity() != null && product.stockQuantity() > 0 ? "IN_STOCK" : "OUT_OF_STOCK";
            return new AdminProductListItemResponse(
                    product.id(),
                    product.name(),
                    product.price(),
                    new CategoryResponse(product.categoryId(), product.categoryName()),
                    product.active(),
                    product.mainImageUrl(),
                    stockIndicator,
                    product.createdAt(),
                    product.updatedAt()
            );
        }
    }

    @Schema(name = "AdminProductDetailResponse")
    public record AdminProductDetailResponse(
            Long id,
            String name,
            String shortDescription,
            String longDescription,
            BigDecimal price,
            CategoryResponse category,
            String mainImageUrl,
            List<String> imageUrls,
            boolean active,
            boolean available,
            Map<String, String> attributes,
            List<ProductVariantResponse> variants,
            Integer stockQuantity,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        static AdminProductDetailResponse fromDomain(AdminProduct product) {
            return new AdminProductDetailResponse(
                    product.id(),
                    product.name(),
                    product.shortDescription(),
                    product.longDescription(),
                    product.price(),
                    new CategoryResponse(product.categoryId(), product.categoryName()),
                    product.mainImageUrl(),
                    product.imageUrls(),
                    product.active(),
                    product.available(),
                    product.attributes(),
                    product.variants().stream().map(ProductVariantResponse::fromDomain).toList(),
                    product.stockQuantity(),
                    product.createdAt(),
                    product.updatedAt()
            );
        }
    }

    @Schema(name = "AdminCategoryResponse")
    public record CategoryResponse(Long id, String name) {
    }

    @Schema(name = "AdminProductVariantResponse")
    public record ProductVariantResponse(String id, String name, Map<String, String> attributes, Integer stockQuantity, Boolean available) {
        static ProductVariantResponse fromDomain(ProductVariant variant) {
            return new ProductVariantResponse(variant.id(), variant.name(), variant.attributes(), variant.stockQuantity(), variant.available());
        }
    }

    @Schema(name = "AdminUpsertProductRequest")
    public record UpsertProductRequest(
            @NotBlank String name,
            @NotBlank String shortDescription,
            String longDescription,
            @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal price,
            @NotNull Long categoryId,
            @NotBlank String mainImageUrl,
            List<String> imageUrls,
            Boolean available,
            Map<String, String> attributes,
            @Valid List<ProductVariantRequest> variants
    ) {
    }

    @Schema(name = "AdminProductVariantRequest")
    public record ProductVariantRequest(String id, String name, Map<String, String> attributes, Integer stockQuantity, Boolean available) {
        ProductVariant toDomain() {
            return new ProductVariant(id, name, attributes, stockQuantity, available);
        }
    }

    @Schema(name = "AdminUpdateProductStatusRequest")
    public record UpdateStatusRequest(@NotNull Boolean active) {
    }

    @Schema(name = "AdminProductErrorResponse")
    public record ErrorResponse(String code, String message) {
    }
}
