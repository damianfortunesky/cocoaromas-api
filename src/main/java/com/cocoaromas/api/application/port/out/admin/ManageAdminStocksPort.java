package com.cocoaromas.api.application.port.out.admin;

import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.AdminStockPage;
import com.cocoaromas.api.domain.admin.stock.AdminStockQuery;

public interface ManageAdminStocksPort {
    AdminStockPage find(AdminStockQuery query, int lowStockThreshold);

    AdminStockDetail getByProductId(Long productId, int lowStockThreshold);

    AdminStockDetail updateSimpleStock(Long productId, int newStockQuantity, int lowStockThreshold);
}
