package com.cocoaromas.api.domain.admin.promotion;

public record AdminPromotionQuery(
        String search,
        PromotionType promotionType,
        Boolean active,
        Boolean currentlyValid,
        int page,
        int size,
        String sortBy,
        String direction
) {
}
