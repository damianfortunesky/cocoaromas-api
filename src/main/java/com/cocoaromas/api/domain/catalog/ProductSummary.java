package com.cocoaromas.api.domain.catalog;

import java.math.BigDecimal;

public record ProductSummary(
        Long id,
        String name,
        String description,
        BigDecimal price,
        ProductCategory category,
        String imageUrl,
        boolean available,
        int stockQuantity
) {
}
