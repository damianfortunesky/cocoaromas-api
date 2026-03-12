package com.cocoaromas.api.infrastructure.persistence.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(name = "short_description", nullable = false, length = 280)
    private String shortDescription;

    @Column(name = "long_description", length = 2000)
    private String longDescription;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "main_image_url", nullable = false, length = 500)
    private String mainImageUrl;

    @Column(name = "image_urls_json")
    private String imageUrlsJson;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "is_visible", nullable = false)
    private Boolean visible;

    @Column(name = "has_variants", nullable = false)
    private Boolean hasVariants;

    @Column(name = "attributes_json")
    private String attributesJson;

    @Column(name = "variants_json")
    private String variantsJson;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public String getImageUrlsJson() {
        return imageUrlsJson;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Boolean getHasVariants() {
        return hasVariants;
    }

    public String getAttributesJson() {
        return attributesJson;
    }

    public String getVariantsJson() {
        return variantsJson;
    }
}
