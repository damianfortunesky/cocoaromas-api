package com.cocoaromas.api.domain.catalog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductDetail(
        Long id,
        String name,
        String shortDescription,
        String longDescription,
        BigDecimal price,
        ProductCategory category,
        String mainImageUrl,
        List<String> imageUrls,
        boolean available,
        int stockQuantity,
        Map<String, String> attributes,
        List<ProductVariant> variants,
        List<RelatedProduct> relatedProducts
) {
}
