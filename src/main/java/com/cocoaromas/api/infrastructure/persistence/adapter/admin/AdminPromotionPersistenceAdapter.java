package com.cocoaromas.api.infrastructure.persistence.adapter.admin;

import com.cocoaromas.api.application.port.out.admin.ManageAdminPromotionsPort;
import com.cocoaromas.api.application.service.admin.AdminPromotionNotFoundException;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionPage;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionQuery;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.promotion.PromotionEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.promotion.PromotionJpaRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AdminPromotionPersistenceAdapter implements ManageAdminPromotionsPort {

    private final PromotionJpaRepository promotionJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public AdminPromotionPersistenceAdapter(
            PromotionJpaRepository promotionJpaRepository,
            ProductJpaRepository productJpaRepository,
            CategoryJpaRepository categoryJpaRepository
    ) {
        this.promotionJpaRepository = promotionJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public AdminPromotionPage find(AdminPromotionQuery query) {
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), mapSort(query.sortBy()));
        var pageable = PageRequest.of(query.page(), query.size(), sort);
        OffsetDateTime now = OffsetDateTime.now();

        var page = promotionJpaRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (query.search() != null && !query.search().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.search().trim().toLowerCase() + "%"));
            }
            if (query.promotionType() != null) {
                predicates.add(cb.equal(root.get("promotionType"), query.promotionType()));
            }
            if (query.active() != null) {
                predicates.add(cb.equal(root.get("active"), query.active()));
            }
            if (query.currentlyValid() != null) {
                Predicate started = cb.or(cb.isNull(root.get("startsAt")), cb.lessThanOrEqualTo(root.get("startsAt"), now));
                Predicate notEnded = cb.or(cb.isNull(root.get("endsAt")), cb.greaterThanOrEqualTo(root.get("endsAt"), now));
                Predicate validity = cb.and(started, notEnded);
                predicates.add(Boolean.TRUE.equals(query.currentlyValid()) ? validity : cb.not(validity));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new AdminPromotionPage(
                page.getContent().stream().map(this::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public AdminPromotion save(AdminPromotion promotion) {
        PromotionEntity entity = promotion.id() == null
                ? new PromotionEntity()
                : promotionJpaRepository.findById(promotion.id()).orElseThrow(() -> new AdminPromotionNotFoundException(promotion.id()));

        entity.setName(promotion.name());
        entity.setDescription(promotion.description());
        entity.setPromotionType(promotion.promotionType());
        entity.setDiscountType(promotion.discountType());
        entity.setDiscountValue(promotion.discountValue());
        entity.setMinimumQuantity(promotion.minimumQuantity());
        entity.setProduct(loadProduct(promotion.productId()));
        entity.setCategory(loadCategory(promotion.categoryId()));
        entity.setActive(promotion.active());
        entity.setStartsAt(promotion.startsAt());
        entity.setEndsAt(promotion.endsAt());

        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(OffsetDateTime.now());
        }
        entity.setUpdatedAt(OffsetDateTime.now());

        return toDomain(promotionJpaRepository.save(entity));
    }

    @Override
    public AdminPromotion getById(Long id) {
        return promotionJpaRepository.findById(id)
                .filter(p -> p.getDeletedAt() == null)
                .map(this::toDomain)
                .orElseThrow(() -> new AdminPromotionNotFoundException(id));
    }

    @Override
    public void softDelete(Long id) {
        PromotionEntity entity = promotionJpaRepository.findById(id)
                .orElseThrow(() -> new AdminPromotionNotFoundException(id));
        entity.setDeletedAt(OffsetDateTime.now());
        entity.setActive(false);
        entity.setUpdatedAt(OffsetDateTime.now());
        promotionJpaRepository.save(entity);
    }

    @Override
    public boolean existsProduct(Long productId) {
        return productJpaRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .isPresent();
    }

    @Override
    public boolean existsCategory(Long categoryId) {
        return categoryJpaRepository.existsById(categoryId);
    }

    private ProductEntity loadProduct(Long productId) {
        return productId == null ? null : productJpaRepository.findById(productId).orElse(null);
    }

    private CategoryEntity loadCategory(Long categoryId) {
        return categoryId == null ? null : categoryJpaRepository.findById(categoryId).orElse(null);
    }

    private AdminPromotion toDomain(PromotionEntity entity) {
        ProductEntity product = entity.getProduct();
        CategoryEntity category = entity.getCategory();
        return new AdminPromotion(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPromotionType(),
                entity.getDiscountType(),
                entity.getDiscountValue(),
                entity.getMinimumQuantity(),
                product == null ? null : product.getId(),
                product == null ? null : product.getName(),
                category == null ? null : category.getId(),
                category == null ? null : category.getName(),
                Boolean.TRUE.equals(entity.getActive()),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private String mapSort(String sortBy) {
        return switch (sortBy) {
            case "name", "discountValue", "startsAt", "endsAt", "createdAt", "updatedAt" -> sortBy;
            default -> "updatedAt";
        };
    }
}
