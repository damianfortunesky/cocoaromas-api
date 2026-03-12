package com.cocoaromas.api.application.service.catalog;

import com.cocoaromas.api.application.port.in.catalog.GetPublicCategoriesUseCase;
import com.cocoaromas.api.application.port.in.catalog.GetPublicProductsUseCase;
import com.cocoaromas.api.application.port.out.catalog.LoadCatalogPort;
import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CatalogService implements GetPublicProductsUseCase, GetPublicCategoriesUseCase {

    private final LoadCatalogPort loadCatalogPort;

    public CatalogService(LoadCatalogPort loadCatalogPort) {
        this.loadCatalogPort = loadCatalogPort;
    }

    @Override
    public ProductCatalogPage getProducts(ProductCatalogQuery query) {
        return loadCatalogPort.findProducts(query);
    }

    @Override
    public List<ProductCategory> getCategories() {
        return loadCatalogPort.findCategories();
    }
}
