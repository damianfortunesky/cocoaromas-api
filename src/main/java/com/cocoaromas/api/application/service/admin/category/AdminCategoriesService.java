package com.cocoaromas.api.application.service.admin.category;

import com.cocoaromas.api.application.port.in.admin.category.AdminCategoriesUseCase;
import com.cocoaromas.api.domain.admin.category.AdminCategory;
import com.cocoaromas.api.domain.admin.category.UpsertAdminCategoryCommand;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminCategoriesService implements AdminCategoriesUseCase {

    private final CategoryJpaRepository categoryJpaRepository;

    public AdminCategoriesService(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public List<AdminCategory> list() {
        return categoryJpaRepository.findAllByOrderByDisplayOrderAscNameAsc().stream().map(this::toDomain).toList();
    }

    @Override
    public AdminCategory create(UpsertAdminCategoryCommand command) {
        validate(command);
        CategoryEntity entity = new CategoryEntity();
        entity.setSlug(command.slug().trim().toLowerCase());
        entity.setName(command.name().trim());
        entity.setDisplayOrder(command.displayOrder() == null ? 0 : command.displayOrder());
        return toDomain(categoryJpaRepository.save(entity));
    }

    @Override
    public AdminCategory update(Long id, UpsertAdminCategoryCommand command) {
        validateForUpdate(id, command);
        CategoryEntity entity = categoryJpaRepository.findById(id).orElseThrow(() -> new AdminCategoryNotFoundException(id));
        entity.setSlug(command.slug().trim().toLowerCase());
        entity.setName(command.name().trim());
        entity.setDisplayOrder(command.displayOrder() == null ? 0 : command.displayOrder());
        return toDomain(categoryJpaRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CategoryEntity entity = categoryJpaRepository.findById(id).orElseThrow(() -> new AdminCategoryNotFoundException(id));
        categoryJpaRepository.delete(entity);
    }

    private void validate(UpsertAdminCategoryCommand command) {
        if (command.slug() == null || command.slug().isBlank()) throw new AdminCategoryValidationException("slug es requerido");
        if (command.name() == null || command.name().isBlank()) throw new AdminCategoryValidationException("name es requerido");

        String normalizedSlug = command.slug().trim().toLowerCase();
        String normalizedName = command.name().trim().toLowerCase();

        if (categoryJpaRepository.existsBySlugIgnoreCase(normalizedSlug)) {
            throw new AdminCategoryValidationException("slug ya existe");
        }

        if (categoryJpaRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new AdminCategoryValidationException("name ya existe");
        }
    }


    private void validateForUpdate(Long id, UpsertAdminCategoryCommand command) {
        if (command.slug() == null || command.slug().isBlank()) throw new AdminCategoryValidationException("slug es requerido");
        if (command.name() == null || command.name().isBlank()) throw new AdminCategoryValidationException("name es requerido");

        String normalizedSlug = command.slug().trim().toLowerCase();
        String normalizedName = command.name().trim().toLowerCase();

        if (categoryJpaRepository.existsBySlugIgnoreCaseAndIdNot(normalizedSlug, id)) {
            throw new AdminCategoryValidationException("slug ya existe");
        }

        if (categoryJpaRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new AdminCategoryValidationException("name ya existe");
        }
    }

    private AdminCategory toDomain(CategoryEntity entity) {
        return new AdminCategory(entity.getId(), entity.getSlug(), entity.getName(), entity.getDisplayOrder());
    }
}
