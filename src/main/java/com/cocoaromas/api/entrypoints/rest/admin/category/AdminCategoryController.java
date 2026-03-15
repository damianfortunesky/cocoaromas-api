package com.cocoaromas.api.entrypoints.rest.admin.category;

import com.cocoaromas.api.application.port.in.admin.category.AdminCategoriesUseCase;
import com.cocoaromas.api.entrypoints.rest.admin.category.AdminCategoryDtos.CategoryResponse;
import com.cocoaromas.api.entrypoints.rest.admin.category.AdminCategoryDtos.UpsertCategoryRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class AdminCategoryController {

    private final AdminCategoriesUseCase adminCategoriesUseCase;

    public AdminCategoryController(AdminCategoriesUseCase adminCategoriesUseCase) {
        this.adminCategoriesUseCase = adminCategoriesUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    public List<CategoryResponse> list() {
        return adminCategoriesUseCase.list().stream().map(CategoryResponse::fromDomain).toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody UpsertCategoryRequest request) {
        return CategoryResponse.fromDomain(adminCategoriesUseCase.create(request.toCommand()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody UpsertCategoryRequest request) {
        return CategoryResponse.fromDomain(adminCategoriesUseCase.update(id, request.toCommand()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        adminCategoriesUseCase.delete(id);
    }
}
