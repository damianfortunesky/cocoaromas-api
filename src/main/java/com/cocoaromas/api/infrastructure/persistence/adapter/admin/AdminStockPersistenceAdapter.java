package com.cocoaromas.api.infrastructure.persistence.adapter.admin;

import com.cocoaromas.api.application.port.out.admin.ManageAdminStocksPort;
import com.cocoaromas.api.application.service.admin.AdminProductNotFoundException;
import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.AdminStockItem;
import com.cocoaromas.api.domain.admin.stock.AdminStockPage;
import com.cocoaromas.api.domain.admin.stock.AdminStockQuery;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AdminStockPersistenceAdapter implements ManageAdminStocksPort {

    private final ProductJpaRepository productJpaRepository;

    public AdminStockPersistenceAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public AdminStockPage find(AdminStockQuery query, int lowStockThreshold) {
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
            if (query.available() != null) {
                if (query.available()) {
                    predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
                } else {
                    predicates.add(cb.lessThanOrEqualTo(root.get("stockQuantity"), 0));
                }
            }
            if (query.lowStock() != null) {
                if (query.lowStock()) {
                    predicates.add(cb.between(root.get("stockQuantity"), 1, lowStockThreshold));
                } else {
                    predicates.add(cb.or(
                            cb.lessThanOrEqualTo(root.get("stockQuantity"), 0),
                            cb.greaterThan(root.get("stockQuantity"), lowStockThreshold)
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new AdminStockPage(
                page.getContent().stream().map(entity -> toItem(entity, lowStockThreshold)).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public AdminStockDetail getByProductId(Long productId, int lowStockThreshold) {
        ProductEntity entity = productJpaRepository.findById(productId)
                .filter(product -> product.getDeletedAt() == null)
                .orElseThrow(() -> new AdminProductNotFoundException(productId));
        return toDetail(entity, lowStockThreshold);
    }

    @Override
    public AdminStockDetail updateSimpleStock(Long productId, int newStockQuantity, int lowStockThreshold) {
        ProductEntity entity = productJpaRepository.findById(productId)
                .filter(product -> product.getDeletedAt() == null)
                .orElseThrow(() -> new AdminProductNotFoundException(productId));

        entity.setStockQuantity(newStockQuantity);
        entity.setAvailable(newStockQuantity > 0);
        entity.setUpdatedAt(java.time.OffsetDateTime.now());

        return toDetail(productJpaRepository.save(entity), lowStockThreshold);
    }

    private AdminStockItem toItem(ProductEntity entity, int lowStockThreshold) {
        int quantity = entity.getStockQuantity() == null ? 0 : entity.getStockQuantity();
        boolean available = quantity > 0;
        return new AdminStockItem(
                entity.getId(),
                entity.getName(),
                entity.getCategory().getName(),
                Boolean.TRUE.equals(entity.getActive()),
                quantity,
                available,
                quantity > 0 && quantity <= lowStockThreshold,
                Boolean.TRUE.equals(entity.getHasVariants()),
                entity.getMainImageUrl()
        );
    }

    private AdminStockDetail toDetail(ProductEntity entity, int lowStockThreshold) {
        int quantity = entity.getStockQuantity() == null ? 0 : entity.getStockQuantity();
        boolean available = quantity > 0;
        return new AdminStockDetail(
                entity.getId(),
                entity.getName(),
                entity.getCategory().getName(),
                Boolean.TRUE.equals(entity.getActive()),
                quantity,
                available,
                quantity > 0 && quantity <= lowStockThreshold,
                Boolean.TRUE.equals(entity.getHasVariants()),
                entity.getMainImageUrl(),
                entity.getUpdatedAt()
        );
    }

    private String mapSort(String sortBy) {
        return switch (sortBy) {
            case "productName" -> "name";
            case "category" -> "category.name";
            case "stockQuantity" -> "stockQuantity";
            case "updatedAt" -> "updatedAt";
            default -> "updatedAt";
        };
    }
}
