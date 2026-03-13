package com.cocoaromas.api.domain.admin.stock;

public record AdminStockItem(
        Long productId,
        String productName,
        String category,
        boolean active,
        Integer stockQuantity,
        boolean available,
        boolean lowStock,
        boolean hasVariants,
        String mainImageUrl
) {
}
