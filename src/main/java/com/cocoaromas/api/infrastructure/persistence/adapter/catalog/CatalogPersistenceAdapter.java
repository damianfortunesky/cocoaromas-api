package com.cocoaromas.api.infrastructure.persistence.adapter.catalog;

import com.cocoaromas.api.application.port.out.catalog.LoadCatalogPort;
import com.cocoaromas.api.domain.catalog.CatalogSortDirection;
import com.cocoaromas.api.domain.catalog.CatalogSortField;
import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductSummary;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CatalogPersistenceAdapter implements LoadCatalogPort {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public CatalogPersistenceAdapter(ProductJpaRepository productJpaRepository, CategoryJpaRepository categoryJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public ProductCatalogPage findProducts(ProductCatalogQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size(), buildSort(query));

        Page<ProductEntity> result = productJpaRepository.findAll((root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("visible")));

            if (query.search() != null && !query.search().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.search().trim().toLowerCase() + "%"));
            }

            if (query.category() != null && !query.category().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("category").get("slug")), query.category().trim().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new ProductCatalogPage(
                result.getContent().stream().map(this::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public List<ProductCategory> findCategories() {
        return categoryJpaRepository.findAllByOrderByDisplayOrderAscNameAsc().stream()
                .map(this::toDomain)
                .toList();
    }

    private Sort buildSort(ProductCatalogQuery query) {
        String property = query.sortField() == CatalogSortField.PRICE ? "price" : "name";
        Sort.Direction direction = query.sortDirection() == CatalogSortDirection.DESC
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, property).and(Sort.by(Sort.Direction.ASC, "id"));
    }

    private ProductSummary toDomain(ProductEntity entity) {
        int stock = entity.getStockQuantity() == null ? 0 : entity.getStockQuantity();
        return new ProductSummary(
                entity.getId(),
                entity.getName(),
                entity.getShortDescription(),
                entity.getPrice(),
                toDomain(entity.getCategory()),
                entity.getMainImageUrl(),
                stock > 0,
                stock
        );
    }

    private ProductCategory toDomain(CategoryEntity entity) {
        return new ProductCategory(entity.getId(), entity.getSlug(), entity.getName());
    }
}
