package com.cocoaromas.api.domain.catalog;

import java.util.List;

public record ProductCatalogPage(
        List<ProductSummary> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
