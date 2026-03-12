package com.cocoaromas.api.domain.admin;

public record AdminProductQuery(
        String search,
        Long categoryId,
        Boolean active,
        int page,
        int size,
        String sortBy,
        String direction
) {
}
