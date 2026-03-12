package com.cocoaromas.api.infrastructure.persistence.repository.catalog;

import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
}
