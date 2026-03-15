package com.cocoaromas.api.domain.admin;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AdminProduct(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        String categoryName,
        Integer stockQuantity,
        String imageUrl,
        boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
