package com.cocoaromas.api.application.service.catalog;

import com.cocoaromas.api.application.port.in.catalog.GetPublicCategoriesUseCase;
import com.cocoaromas.api.application.port.in.catalog.GetPublicProductDetailUseCase;
import com.cocoaromas.api.application.port.in.catalog.GetPublicProductsUseCase;
import com.cocoaromas.api.application.port.out.catalog.LoadCatalogPort;
import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductDetail;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CatalogService implements GetPublicProductsUseCase, GetPublicCategoriesUseCase, GetPublicProductDetailUseCase {

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

    @Override
    public ProductDetail getProductDetail(Long productId) {
        return loadCatalogPort.findProductDetailById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
