package com.cocoaromas.api.application.port.in.admin;

import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.admin.AdminProductQuery;
import com.cocoaromas.api.domain.admin.UpsertAdminProductCommand;

public interface AdminProductsUseCase {
    AdminProductPage list(AdminProductQuery query);

    AdminProduct create(UpsertAdminProductCommand command);

    AdminProduct update(Long id, UpsertAdminProductCommand command);

    void delete(Long id);

    AdminProduct updateStatus(Long id, boolean active);
}
