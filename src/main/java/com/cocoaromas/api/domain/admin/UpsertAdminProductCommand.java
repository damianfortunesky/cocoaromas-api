package com.cocoaromas.api.domain.admin;

import com.cocoaromas.api.domain.catalog.ProductVariant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record UpsertAdminProductCommand(
        String name,
        String shortDescription,
        String longDescription,
        BigDecimal price,
        Long categoryId,
        String mainImageUrl,
        List<String> imageUrls,
        Boolean available,
        Map<String, String> attributes,
        List<ProductVariant> variants
) {
}
