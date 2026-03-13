package com.cocoaromas.api.infrastructure.persistence.repository.promotion;

import com.cocoaromas.api.infrastructure.persistence.entity.promotion.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromotionJpaRepository extends JpaRepository<PromotionEntity, Long>, JpaSpecificationExecutor<PromotionEntity> {
}
