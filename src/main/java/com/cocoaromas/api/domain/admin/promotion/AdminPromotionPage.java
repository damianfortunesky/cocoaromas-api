package com.cocoaromas.api.domain.admin.promotion;

import java.util.List;

public record AdminPromotionPage(
        List<AdminPromotion> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
