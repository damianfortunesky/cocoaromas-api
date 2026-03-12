package com.cocoaromas.api.domain.order;

import java.util.List;

public record CustomerOrderPage(
        List<CustomerOrderSummary> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
