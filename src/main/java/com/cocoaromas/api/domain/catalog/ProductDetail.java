package com.cocoaromas.api.domain.catalog;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetail(
        Long id,
        String name,
        String description,
        BigDecimal price,
        ProductCategory category,
        String imageUrl,
        boolean available,
        int stockQuantity,
        List<RelatedProduct> relatedProducts
) {
}
