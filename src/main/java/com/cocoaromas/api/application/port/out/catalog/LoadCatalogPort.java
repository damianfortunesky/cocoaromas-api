package com.cocoaromas.api.application.port.out.catalog;

import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import java.util.List;

public interface LoadCatalogPort {
    ProductCatalogPage findProducts(ProductCatalogQuery query);

    List<ProductCategory> findCategories();
}
