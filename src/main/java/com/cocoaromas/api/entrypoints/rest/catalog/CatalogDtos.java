package com.cocoaromas.api.entrypoints.rest.catalog;

import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

public final class CatalogDtos {

    private CatalogDtos() {
    }

    @Schema(name = "PublicProductResponse", description = "Producto visible en el catálogo público")
    public record ProductResponse(
            Long id,
            String name,
            String shortDescription,
            BigDecimal price,
            CategoryResponse category,
            String mainImageUrl,
            boolean available,
            int stockQuantity
    ) {
        static ProductResponse fromDomain(ProductSummary product) {
            return new ProductResponse(
                    product.id(),
                    product.name(),
                    product.shortDescription(),
                    product.price(),
                    CategoryResponse.fromDomain(product.category()),
                    product.mainImageUrl(),
                    product.available(),
                    product.stockQuantity()
            );
        }
    }

    @Schema(name = "PublicCategoryResponse", description = "Categoría pública de catálogo")
    public record CategoryResponse(
            Long id,
            String slug,
            String name
    ) {
        static CategoryResponse fromDomain(ProductCategory category) {
            return new CategoryResponse(category.id(), category.slug(), category.name());
        }
    }

    @Schema(name = "PublicProductsPageResponse", description = "Página de productos del catálogo")
    public record ProductsPageResponse(
            List<ProductResponse> items,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        static ProductsPageResponse fromDomain(ProductCatalogPage page) {
            return new ProductsPageResponse(
                    page.items().stream().map(ProductResponse::fromDomain).toList(),
                    page.page(),
                    page.size(),
                    page.totalElements(),
                    page.totalPages()
            );
        }
    }
}
