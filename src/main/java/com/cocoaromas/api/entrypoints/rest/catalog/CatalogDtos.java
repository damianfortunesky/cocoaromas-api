package com.cocoaromas.api.entrypoints.rest.catalog;

import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductDetail;
import com.cocoaromas.api.domain.catalog.ProductSummary;
import com.cocoaromas.api.domain.catalog.RelatedProduct;
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
            String description,
            BigDecimal price,
            CategoryResponse category,
            String imageUrl,
            boolean available,
            int stockQuantity
    ) {
        static ProductResponse fromDomain(ProductSummary product) {
            return new ProductResponse(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    CategoryResponse.fromDomain(product.category()),
                    product.imageUrl(),
                    product.available(),
                    product.stockQuantity()
            );
        }
    }

    @Schema(name = "PublicProductDetailResponse", description = "Detalle público de producto para la pantalla de PDP")
    public record ProductDetailResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            CategoryResponse category,
            String imageUrl,
            boolean available,
            int stockQuantity,
            List<RelatedProductResponse> relatedProducts
    ) {
        static ProductDetailResponse fromDomain(ProductDetail product) {
            return new ProductDetailResponse(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    CategoryResponse.fromDomain(product.category()),
                    product.imageUrl(),
                    product.available(),
                    product.stockQuantity(),
                    product.relatedProducts().stream().map(RelatedProductResponse::fromDomain).toList()
            );
        }
    }

    @Schema(name = "RelatedProductResponse", description = "Producto relacionado sugerido")
    public record RelatedProductResponse(
            Long id,
            String name,
            BigDecimal price,
            String imageUrl,
            boolean available
    ) {
        static RelatedProductResponse fromDomain(RelatedProduct product) {
            return new RelatedProductResponse(
                    product.id(),
                    product.name(),
                    product.price(),
                    product.imageUrl(),
                    product.available()
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

    @Schema(name = "CatalogErrorResponse", description = "Error en endpoints públicos de catálogo")
    public record ErrorResponse(
            String code,
            String message
    ) {
    }
}
