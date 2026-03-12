package com.cocoaromas.api.infrastructure.persistence.repository.order;

import com.cocoaromas.api.infrastructure.persistence.entity.order.OrderEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    Optional<OrderEntity> findWithItemsById(Long id);
}
