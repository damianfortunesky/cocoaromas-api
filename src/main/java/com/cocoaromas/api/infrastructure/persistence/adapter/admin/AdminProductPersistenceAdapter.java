package com.cocoaromas.api.infrastructure.persistence.adapter.admin;

import com.cocoaromas.api.application.port.out.admin.ManageAdminProductsPort;
import com.cocoaromas.api.application.service.admin.AdminProductNotFoundException;
import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.admin.AdminProductQuery;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductVariant;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AdminProductPersistenceAdapter implements ManageAdminProductsPort {

    private static final TypeReference<List<String>> LIST_STRING = new TypeReference<>() {};
    private static final TypeReference<Map<String, String>> MAP_STRING_STRING = new TypeReference<>() {};
    private static final TypeReference<List<VariantJson>> LIST_VARIANT_JSON = new TypeReference<>() {};

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final ObjectMapper objectMapper;

    public AdminProductPersistenceAdapter(
            ProductJpaRepository productJpaRepository,
            CategoryJpaRepository categoryJpaRepository,
            ObjectMapper objectMapper
    ) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.objectMapper = objectMapper;
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
        entity.setShortDescription(product.shortDescription());
        entity.setLongDescription(product.longDescription());
        entity.setPrice(product.price());
        entity.setCategory(category);
        entity.setMainImageUrl(product.mainImageUrl());
        entity.setImageUrlsJson(writeJson(product.imageUrls()));
        entity.setAttributesJson(writeJson(product.attributes()));
        entity.setVariantsJson(writeJson(product.variants()));
        entity.setActive(product.active());
        entity.setVisible(product.active());
        entity.setAvailable(product.available());
        entity.setHasVariants(product.variants() != null && !product.variants().isEmpty());
        if (entity.getStockQuantity() == null) {
            entity.setStockQuantity(product.stockQuantity() == null ? 0 : product.stockQuantity());
        }
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
        entity.setVisible(false);
        entity.setUpdatedAt(OffsetDateTime.now());
        productJpaRepository.save(entity);
    }

    @Override
    public boolean existsCategory(Long categoryId) {
        return categoryJpaRepository.existsById(categoryId);
    }

    private String mapSort(String sortBy) {
        return switch (sortBy) {
            case "name", "price", "createdAt", "updatedAt" -> switch (sortBy) {
                case "createdAt" -> "createdAt";
                case "updatedAt" -> "updatedAt";
                default -> sortBy;
            };
            default -> "updatedAt";
        };
    }

    private AdminProduct toDomain(ProductEntity entity) {
        ProductCategory category = new ProductCategory(entity.getCategory().getId(), entity.getCategory().getSlug(), entity.getCategory().getName());
        return new AdminProduct(
                entity.getId(),
                entity.getName(),
                entity.getShortDescription(),
                entity.getLongDescription(),
                entity.getPrice(),
                category.id(),
                category.name(),
                entity.getMainImageUrl(),
                parseJson(entity.getImageUrlsJson(), LIST_STRING, Collections.emptyList()),
                Boolean.TRUE.equals(entity.getActive()),
                Boolean.TRUE.equals(entity.getAvailable()),
                parseJson(entity.getAttributesJson(), MAP_STRING_STRING, Collections.emptyMap()),
                parseVariants(entity.getVariantsJson()),
                entity.getStockQuantity(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private List<ProductVariant> parseVariants(String rawJson) {
        List<VariantJson> variantsJson = parseJson(rawJson, LIST_VARIANT_JSON, Collections.emptyList());
        return variantsJson.stream()
                .map(variantJson -> new ProductVariant(
                        variantJson.id,
                        variantJson.name,
                        variantJson.attributes == null ? Collections.emptyMap() : variantJson.attributes,
                        variantJson.stockQuantity,
                        variantJson.available
                ))
                .toList();
    }

    private String writeJson(Object raw) {
        try {
            return raw == null ? null : objectMapper.writeValueAsString(raw);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Formato JSON inválido");
        }
    }

    private <T> T parseJson(String rawJson, TypeReference<T> typeReference, T fallback) {
        if (rawJson == null || rawJson.isBlank()) {
            return fallback;
        }

        try {
            return objectMapper.readValue(rawJson, typeReference);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private static class VariantJson {
        public String id;
        public String name;
        public Map<String, String> attributes;
        public Integer stockQuantity;
        public Boolean available;
    }
}
