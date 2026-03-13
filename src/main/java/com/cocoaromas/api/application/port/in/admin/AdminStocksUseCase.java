package com.cocoaromas.api.application.port.in.admin;

import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.AdminStockPage;
import com.cocoaromas.api.domain.admin.stock.AdminStockQuery;
import com.cocoaromas.api.domain.admin.stock.UpdateAdminStockCommand;

public interface AdminStocksUseCase {
    AdminStockPage list(AdminStockQuery query);

    AdminStockDetail getByProductId(Long productId);

    AdminStockDetail updateStock(Long productId, UpdateAdminStockCommand command);
}
