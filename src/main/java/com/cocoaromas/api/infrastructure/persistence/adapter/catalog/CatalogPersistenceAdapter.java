package com.cocoaromas.api.infrastructure.persistence.adapter.catalog;

import com.cocoaromas.api.application.port.out.catalog.LoadCatalogPort;
import com.cocoaromas.api.domain.catalog.CatalogSortDirection;
import com.cocoaromas.api.domain.catalog.CatalogSortField;
import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductDetail;
import com.cocoaromas.api.domain.catalog.ProductSummary;
import com.cocoaromas.api.domain.catalog.RelatedProduct;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CatalogPersistenceAdapter implements LoadCatalogPort {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public CatalogPersistenceAdapter(
            ProductJpaRepository productJpaRepository,
            CategoryJpaRepository categoryJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public ProductCatalogPage findProducts(ProductCatalogQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size(), buildSort(query));

        Page<ProductEntity> result = productJpaRepository.findAll((root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (query.search() != null && !query.search().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.search().trim().toLowerCase() + "%"));
            }

            if (query.category() != null && !query.category().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("category").get("slug")), query.category().trim().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new ProductCatalogPage(
                result.getContent().stream().map(this::toSummaryDomain).toList(),
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

    @Override
    public Optional<ProductDetail> findProductDetailById(Long productId) {
        return productJpaRepository.findById(productId)
                .filter(product -> Boolean.TRUE.equals(product.getActive()) && product.getDeletedAt() == null)
                .map(product -> {
                    List<RelatedProduct> relatedProducts = productJpaRepository
                            .findTop4ByActiveTrueAndDeletedAtIsNullAndCategoryIdAndIdNotOrderByIdAsc(product.getCategory().getId(), product.getId())
                            .stream()
                            .map(this::toRelatedProduct)
                            .toList();

                    return toDetailDomain(product, relatedProducts);
                });
    }

    private Sort buildSort(ProductCatalogQuery query) {
        String property = query.sortField() == CatalogSortField.PRICE ? "price" : "name";
        Sort.Direction direction = query.sortDirection() == CatalogSortDirection.DESC
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, property).and(Sort.by(Sort.Direction.ASC, "id"));
    }

    private ProductSummary toSummaryDomain(ProductEntity entity) {
        int stock = entity.getStockQuantity() == null ? 0 : entity.getStockQuantity();
        return new ProductSummary(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                toDomain(entity.getCategory()),
                entity.getImageUrl(),
                stock > 0,
                stock
        );
    }

    private ProductDetail toDetailDomain(ProductEntity entity, List<RelatedProduct> relatedProducts) {
        int stock = entity.getStockQuantity() == null ? 0 : entity.getStockQuantity();
        return new ProductDetail(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                toDomain(entity.getCategory()),
                entity.getImageUrl(),
                stock > 0,
                stock,
                relatedProducts
        );
    }

    private RelatedProduct toRelatedProduct(ProductEntity entity) {
        int stock = entity.getStockQuantity() == null ? 0 : entity.getStockQuantity();
        return new RelatedProduct(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl(),
                stock > 0
        );
    }

    private ProductCategory toDomain(CategoryEntity entity) {
        return new ProductCategory(entity.getId(), entity.getSlug(), entity.getName());
    }
}
