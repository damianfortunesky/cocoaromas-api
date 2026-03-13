package com.cocoaromas.api.infrastructure.persistence.adapter.admin;

import com.cocoaromas.api.application.port.out.admin.ManageAdminOrdersPort;
import com.cocoaromas.api.application.service.admin.AdminOrderNotFoundException;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetail;
import com.cocoaromas.api.domain.admin.order.AdminOrderDetailItem;
import com.cocoaromas.api.domain.admin.order.AdminOrderItem;
import com.cocoaromas.api.domain.admin.order.AdminOrderPage;
import com.cocoaromas.api.domain.admin.order.AdminOrderQuery;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.infrastructure.persistence.entity.order.OrderEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.order.OrderItemEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.order.OrderJpaRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AdminOrderPersistenceAdapter implements ManageAdminOrdersPort {

    private final OrderJpaRepository orderJpaRepository;

    public AdminOrderPersistenceAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public AdminOrderPage find(AdminOrderQuery query) {
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), mapSort(query.sortBy()));
        var pageable = PageRequest.of(query.page(), query.size(), sort);

        var page = orderJpaRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.search() != null && !query.search().isBlank()) {
                String like = "%" + query.search().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("user").get("email")), like));
            }
            if (query.status() != null) {
                predicates.add(cb.equal(root.get("status"), query.status()));
            }
            if (query.paymentMethod() != null) {
                predicates.add(cb.equal(root.get("paymentMethod"), query.paymentMethod()));
            }
            if (query.createdFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), query.createdFrom()));
            }
            if (query.createdTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), query.createdTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new AdminOrderPage(
                page.getContent().stream().map(this::toItem).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public Optional<AdminOrderDetail> findById(Long orderId) {
        return orderJpaRepository.findWithItemsById(orderId).map(this::toDetail);
    }

    @Override
    public AdminOrderDetail updateStatus(Long orderId, OrderStatus newStatus, String reason) {
        OrderEntity order = orderJpaRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new AdminOrderNotFoundException(orderId));

        order.setStatus(newStatus);
        order.setStatusReason(reason);
        order.setUpdatedAt(OffsetDateTime.now());

        return toDetail(orderJpaRepository.save(order));
    }

    private AdminOrderItem toItem(OrderEntity entity) {
        return new AdminOrderItem(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getStatus(),
                entity.getPaymentMethod(),
                entity.getUser().getId(),
                resolveCustomerName(entity),
                entity.getUser().getEmail(),
                entity.getTotal(),
                entity.getItems().stream().mapToInt(OrderItemEntity::getQuantity).sum()
        );
    }

    private AdminOrderDetail toDetail(OrderEntity entity) {
        BigDecimal subtotal = entity.getItems().stream()
                .map(OrderItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discounts = subtotal.subtract(entity.getTotal());

        return new AdminOrderDetail(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getStatus(),
                entity.getPaymentMethod(),
                entity.getUser().getId(),
                resolveCustomerName(entity),
                entity.getUser().getEmail(),
                subtotal,
                discounts,
                entity.getTotal(),
                entity.getNotes(),
                entity.getItems().stream().map(item -> new AdminOrderDetailItem(
                        item.getProduct().getId(),
                        item.getProductName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getSubtotal(),
                        item.getProduct().getMainImageUrl()
                )).toList()
        );
    }


    private String resolveCustomerName(OrderEntity entity) {
        String email = entity.getUser().getEmail();
        if (email == null || email.isBlank()) {
            return "Cliente";
        }

        int atIndex = email.indexOf('@');
        String localPart = atIndex > 0 ? email.substring(0, atIndex) : email;
        return localPart.isBlank() ? email : localPart;
    }

    private String mapSort(String sortBy) {
        return switch (sortBy) {
            case "orderId" -> "id";
            case "status" -> "status";
            case "total" -> "total";
            default -> "createdAt";
        };
    }
}
