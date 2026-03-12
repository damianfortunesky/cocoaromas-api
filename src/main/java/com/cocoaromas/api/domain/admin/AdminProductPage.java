package com.cocoaromas.api.domain.admin;

import java.util.List;

public record AdminProductPage(
        List<AdminProduct> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
