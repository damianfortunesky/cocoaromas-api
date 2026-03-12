package com.cocoaromas.api.application.port.out.admin;

import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.admin.AdminProductQuery;

public interface ManageAdminProductsPort {
    AdminProductPage find(AdminProductQuery query);

    AdminProduct save(AdminProduct product);

    AdminProduct getById(Long id);

    void softDelete(Long id);

    boolean existsCategory(Long categoryId);
}
