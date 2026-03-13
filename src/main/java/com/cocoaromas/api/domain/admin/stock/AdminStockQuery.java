package com.cocoaromas.api.domain.admin.stock;

public record AdminStockQuery(
        String search,
        Long categoryId,
        Boolean available,
        Boolean lowStock,
        int page,
        int size,
        String sortBy,
        String direction
) {
}
