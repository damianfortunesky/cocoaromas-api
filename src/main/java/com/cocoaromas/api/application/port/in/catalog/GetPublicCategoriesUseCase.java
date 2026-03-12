package com.cocoaromas.api.application.port.in.catalog;

import com.cocoaromas.api.domain.catalog.ProductCategory;
import java.util.List;

public interface GetPublicCategoriesUseCase {
    List<ProductCategory> getCategories();
}
