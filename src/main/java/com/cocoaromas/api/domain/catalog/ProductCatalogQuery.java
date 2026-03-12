package com.cocoaromas.api.domain.catalog;

public record ProductCatalogQuery(
        String search,
        String category,
        CatalogSortField sortField,
        CatalogSortDirection sortDirection,
        int page,
        int size
) {
}
