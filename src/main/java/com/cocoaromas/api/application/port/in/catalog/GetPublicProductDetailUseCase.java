package com.cocoaromas.api.application.port.in.catalog;

import com.cocoaromas.api.domain.catalog.ProductDetail;

public interface GetPublicProductDetailUseCase {
    ProductDetail getProductDetail(Long productId);
}
