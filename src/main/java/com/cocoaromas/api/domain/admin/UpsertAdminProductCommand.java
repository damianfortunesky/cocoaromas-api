package com.cocoaromas.api.domain.admin;

import java.math.BigDecimal;

public record UpsertAdminProductCommand(
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        Integer stockQuantity,
        String imageUrl,
        Boolean isActive
) {
}
