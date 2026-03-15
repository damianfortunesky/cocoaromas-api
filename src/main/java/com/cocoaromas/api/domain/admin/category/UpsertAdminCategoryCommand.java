package com.cocoaromas.api.domain.admin.category;

public record UpsertAdminCategoryCommand(String slug, String name, Integer displayOrder) {
}
