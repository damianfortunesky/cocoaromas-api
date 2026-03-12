package com.cocoaromas.api.application.service.catalog;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId) {
        super("No existe un producto con id " + productId);
    }
}
