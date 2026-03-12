package com.cocoaromas.api.domain.catalog;

import java.util.Map;

public record ProductVariant(
        String id,
        String name,
        Map<String, String> attributes,
        Integer stockQuantity,
        Boolean available
) {
}
