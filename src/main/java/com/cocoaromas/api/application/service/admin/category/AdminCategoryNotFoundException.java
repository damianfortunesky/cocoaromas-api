package com.cocoaromas.api.application.service.admin.category;

public class AdminCategoryNotFoundException extends RuntimeException {
    public AdminCategoryNotFoundException(Long id) { super("Categoría no encontrada: " + id); }
}
