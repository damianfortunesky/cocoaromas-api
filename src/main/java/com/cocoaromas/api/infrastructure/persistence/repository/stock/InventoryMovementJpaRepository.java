package com.cocoaromas.api.infrastructure.persistence.repository.stock;

import com.cocoaromas.api.infrastructure.persistence.entity.stock.InventoryMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementEntity, Long> {
}
