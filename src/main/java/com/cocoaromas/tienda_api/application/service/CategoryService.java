package com.cocoaromas.tienda_api.application.service;

import com.cocoaromas.tienda_api.application.dto.request.CreateCategoryRequest;
import com.cocoaromas.tienda_api.application.dto.response.CategoryResponse;
import com.cocoaromas.tienda_api.application.port.out.CategoryRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepositoryPort categoryRepo;

    public CategoryService(CategoryRepositoryPort categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public CategoryResponse create(CreateCategoryRequest request) {
        var category = categoryRepo.save(
                request.getName(),
                request.getDescription()
        );

        return toResponse(category);
    }

    public List<CategoryResponse> listActive() {
        return categoryRepo.findActive()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CategoryResponse> listAll() {
        return categoryRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void desactivate(Integer categoryId) {
        categoryRepo.desactivate(categoryId);
    }

    private CategoryResponse toResponse(com.cocoaromas.tienda_api.domain.model.Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getDescription(),
                c.isActive()
        );
    }


}

