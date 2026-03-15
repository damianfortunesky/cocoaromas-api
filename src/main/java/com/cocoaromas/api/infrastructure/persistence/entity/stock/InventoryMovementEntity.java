package com.cocoaromas.api.infrastructure.persistence.entity.stock;

import com.cocoaromas.api.infrastructure.persistence.entity.UserEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "movement_type", nullable = false, length = 30)
    private String movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 40)
    private String referenceType;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private UserEntity createdByUser;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public void setProduct(ProductEntity product) { this.product = product; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedByUser(UserEntity createdByUser) { this.createdByUser = createdByUser; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
