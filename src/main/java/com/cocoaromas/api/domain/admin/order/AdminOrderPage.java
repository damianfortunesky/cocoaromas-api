package com.cocoaromas.api.domain.admin.order;

import java.util.List;

public record AdminOrderPage(
        List<AdminOrderItem> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
