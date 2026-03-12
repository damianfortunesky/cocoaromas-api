package com.cocoaromas.api.domain.catalog;

import java.math.BigDecimal;

public record RelatedProduct(
        Long id,
        String name,
        BigDecimal price,
        String mainImageUrl,
        boolean available
) {
}
