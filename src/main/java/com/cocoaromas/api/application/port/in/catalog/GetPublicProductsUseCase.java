package com.cocoaromas.api.application.port.in.catalog;

import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;

public interface GetPublicProductsUseCase {
    ProductCatalogPage getProducts(ProductCatalogQuery query);
}
