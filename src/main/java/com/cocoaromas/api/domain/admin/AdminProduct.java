package com.cocoaromas.api.domain.admin;

import com.cocoaromas.api.domain.catalog.ProductVariant;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record AdminProduct(
        Long id,
        String name,
        String shortDescription,
        String longDescription,
        BigDecimal price,
        Long categoryId,
        String categoryName,
        String mainImageUrl,
        List<String> imageUrls,
        boolean active,
        boolean available,
        Map<String, String> attributes,
        List<ProductVariant> variants,
        Integer stockQuantity,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
