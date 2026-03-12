package com.cocoaromas.api.domain.catalog;

import java.math.BigDecimal;

public record ProductSummary(
        Long id,
        String name,
        String shortDescription,
        BigDecimal price,
        ProductCategory category,
        String mainImageUrl,
        boolean available,
        int stockQuantity
) {
}
