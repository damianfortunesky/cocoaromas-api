package com.cocoaromas.api.application.port.out.order;

import java.math.BigDecimal;
import java.util.Optional;

public interface LoadProductsForOrderPort {
    Optional<ProductForOrder> findById(Long productId);

    record ProductForOrder(
            Long id,
            String name,
            BigDecimal price,
            Integer stockQuantity,
            boolean hasVariants,
            String variantsJson,
            boolean visible
    ) {
    }
}
