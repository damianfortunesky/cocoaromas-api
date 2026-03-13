package com.cocoaromas.api.domain.admin.promotion;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record UpsertAdminPromotionCommand(
        String name,
        String description,
        PromotionType promotionType,
        DiscountType discountType,
        BigDecimal discountValue,
        Integer minimumQuantity,
        Long productId,
        Long categoryId,
        Boolean active,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt
) {
}
