package com.cocoaromas.tienda_api.infrastructure.persistence.adapter;

import com.cocoaromas.tienda_api.application.port.out.CategoryRepositoryPort;
import com.cocoaromas.tienda_api.domain.model.Category;
import com.cocoaromas.tienda_api.infrastructure.persistence.entity.CategoryEntity;
import com.cocoaromas.tienda_api.infrastructure.persistence.repository.JpaCategoryRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final JpaCategoryRepository repo;

    public CategoryRepositoryAdapter(JpaCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Category save(String name, String description) {
        
        CategoryEntity entity = new CategoryEntity();
        
        entity.setName(name);
        entity.setDescription(description);
        entity.setActive(true);

        CategoryEntity saved = repo.save(entity);

        return mapToDomain(saved);
    }

    @Override
    public List<Category> findActive() {
        return repo.findByActiveTrue()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    @Transactional
    public void desactivate(Integer categoryId) {
        CategoryEntity entity = repo.findById(categoryId)
            .orElseThrow(() -> new IllegalStateException(
                "Category not found: " + categoryId
            ));

        entity.setActive(false);
        repo.save(entity);
    }

    private Category mapToDomain(CategoryEntity e) {
        return new Category(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.isActive()
        );
    }
}
