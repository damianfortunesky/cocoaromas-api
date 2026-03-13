package com.cocoaromas.api.domain.admin.stock;

public record UpdateAdminStockCommand(
        Integer newStockQuantity,
        Integer adjustment,
        String reason
) {
}
