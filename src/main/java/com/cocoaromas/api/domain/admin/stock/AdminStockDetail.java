package com.cocoaromas.api.domain.admin.stock;

import java.time.OffsetDateTime;

public record AdminStockDetail(
        Long productId,
        String productName,
        String category,
        boolean active,
        Integer stockQuantity,
        boolean available,
        boolean lowStock,
        String imageUrl,
        OffsetDateTime updatedAt
) {
}
