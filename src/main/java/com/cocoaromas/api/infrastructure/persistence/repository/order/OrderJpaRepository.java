package com.cocoaromas.api.infrastructure.persistence.repository.order;

import com.cocoaromas.api.infrastructure.persistence.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
