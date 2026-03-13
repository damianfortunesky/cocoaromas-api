package com.cocoaromas.api.domain.admin.stock;

import java.util.List;

public record AdminStockPage(
        List<AdminStockItem> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
