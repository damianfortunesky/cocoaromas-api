package com.cocoaromas.api.domain.admin.promotion;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AdminPromotion(
        Long id,
        String name,
        String description,
        PromotionType promotionType,
        DiscountType discountType,
        BigDecimal discountValue,
        Integer minimumQuantity,
        Long productId,
        String productName,
        Long categoryId,
        String categoryName,
        boolean active,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
