package com.cocoaromas.api.application.port.in.admin.category;

import com.cocoaromas.api.domain.admin.category.AdminCategory;
import com.cocoaromas.api.domain.admin.category.UpsertAdminCategoryCommand;
import java.util.List;

public interface AdminCategoriesUseCase {
    List<AdminCategory> list();
    AdminCategory create(UpsertAdminCategoryCommand command);
    AdminCategory update(Long id, UpsertAdminCategoryCommand command);
    void delete(Long id);
}
