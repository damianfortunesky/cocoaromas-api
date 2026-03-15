package com.cocoaromas.api.entrypoints.rest.admin.category;

import com.cocoaromas.api.domain.admin.category.AdminCategory;
import com.cocoaromas.api.domain.admin.category.UpsertAdminCategoryCommand;
import jakarta.validation.constraints.NotBlank;

public final class AdminCategoryDtos {

    private AdminCategoryDtos() {}

    public record CategoryResponse(Long id, String slug, String name, Integer displayOrder) {
        public static CategoryResponse fromDomain(AdminCategory category) {
            return new CategoryResponse(category.id(), category.slug(), category.name(), category.displayOrder());
        }
    }

    public record UpsertCategoryRequest(@NotBlank String slug, @NotBlank String name, Integer displayOrder) {
        public UpsertAdminCategoryCommand toCommand() {
            return new UpsertAdminCategoryCommand(slug, name, displayOrder);
        }
    }

    public record ErrorResponse(String code, String message) {}
}
