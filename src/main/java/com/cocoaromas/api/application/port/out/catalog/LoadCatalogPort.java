package com.cocoaromas.api.application.port.out.catalog;

import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductDetail;
import java.util.List;
import java.util.Optional;

public interface LoadCatalogPort {
    ProductCatalogPage findProducts(ProductCatalogQuery query);

    List<ProductCategory> findCategories();

    Optional<ProductDetail> findProductDetailById(Long productId);
}
