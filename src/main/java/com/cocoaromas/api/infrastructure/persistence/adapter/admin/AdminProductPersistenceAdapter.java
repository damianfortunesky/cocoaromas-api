package com.cocoaromas.api.infrastructure.persistence.adapter.admin;

import com.cocoaromas.api.application.port.out.admin.ManageAdminProductsPort;
import com.cocoaromas.api.application.service.admin.AdminProductNotFoundException;
import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.admin.AdminProductQuery;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AdminProductPersistenceAdapter implements ManageAdminProductsPort {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public AdminProductPersistenceAdapter(
            ProductJpaRepository productJpaRepository,
            CategoryJpaRepository categoryJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public AdminProductPage find(AdminProductQuery query) {
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), mapSort(query.sortBy()));
        var pageable = PageRequest.of(query.page(), query.size(), sort);
        var page = productJpaRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt")));
            if (query.search() != null && !query.search().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.search().trim().toLowerCase() + "%"));
            }
            if (query.categoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), query.categoryId()));
            }
            if (query.active() != null) {
                predicates.add(cb.equal(root.get("active"), query.active()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new AdminProductPage(
                page.getContent().stream().map(this::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public AdminProduct save(AdminProduct product) {
        ProductEntity entity = product.id() == null
                ? new ProductEntity()
                : productJpaRepository.findById(product.id()).orElseThrow(() -> new AdminProductNotFoundException(product.id()));
        CategoryEntity category = categoryJpaRepository.findById(product.categoryId())
                .orElseThrow(() -> new AdminProductNotFoundException(product.categoryId()));

        entity.setName(product.name());
        entity.setDescription(product.description());
        entity.setPrice(product.price());
        entity.setCategory(category);
        entity.setImageUrl(product.imageUrl());
        entity.setStockQuantity(product.stockQuantity());
        entity.setActive(product.isActive());
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(OffsetDateTime.now());
        }
        entity.setUpdatedAt(OffsetDateTime.now());

        return toDomain(productJpaRepository.save(entity));
    }

    @Override
    public AdminProduct getById(Long id) {
        return productJpaRepository.findById(id)
                .filter(p -> p.getDeletedAt() == null)
                .map(this::toDomain)
                .orElseThrow(() -> new AdminProductNotFoundException(id));
    }

    @Override
    public void softDelete(Long id) {
        ProductEntity entity = productJpaRepository.findById(id).orElseThrow(() -> new AdminProductNotFoundException(id));
        entity.setDeletedAt(OffsetDateTime.now());
        entity.setActive(false);
        entity.setUpdatedAt(OffsetDateTime.now());
        productJpaRepository.save(entity);
    }

    @Override
    public boolean existsCategory(Long categoryId) {
        return categoryJpaRepository.existsById(categoryId);
    }

    private String mapSort(String sortBy) {
        return switch (sortBy) {
            case "name", "price", "createdAt", "updatedAt" -> sortBy;
            default -> "updatedAt";
        };
    }

    private AdminProduct toDomain(ProductEntity entity) {
        return new AdminProduct(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory().getId(),
                entity.getCategory().getName(),
                entity.getStockQuantity(),
                entity.getImageUrl(),
                Boolean.TRUE.equals(entity.getActive()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
