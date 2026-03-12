package com.cocoaromas.api.infrastructure.persistence.adapter.order;

import com.cocoaromas.api.application.port.out.order.LoadCustomerOrdersPort;
import com.cocoaromas.api.application.port.out.order.LoadProductsForOrderPort;
import com.cocoaromas.api.application.port.out.order.SaveOrderPort;
import com.cocoaromas.api.domain.order.CreatedOrder;
import com.cocoaromas.api.domain.order.CreatedOrderItem;
import com.cocoaromas.api.domain.order.CustomerOrderDetail;
import com.cocoaromas.api.domain.order.CustomerOrderItemDetail;
import com.cocoaromas.api.domain.order.CustomerOrderPage;
import com.cocoaromas.api.domain.order.CustomerOrderSummary;
import com.cocoaromas.api.infrastructure.persistence.entity.UserEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.catalog.ProductEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.order.OrderEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.order.OrderItemEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.UserJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.ProductJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.order.OrderJpaRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceAdapter implements LoadProductsForOrderPort, SaveOrderPort, LoadCustomerOrdersPort {

    private final ProductJpaRepository productJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    public OrderPersistenceAdapter(
            ProductJpaRepository productJpaRepository,
            UserJpaRepository userJpaRepository,
            OrderJpaRepository orderJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public java.util.Optional<ProductForOrder> findById(Long productId) {
        return productJpaRepository.findById(productId)
                .map(product -> new ProductForOrder(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        Boolean.TRUE.equals(product.getHasVariants()),
                        product.getVariantsJson(),
                        Boolean.TRUE.equals(product.getVisible())
                ));
    }

    @Override
    public CreatedOrder save(OrderToSave order) {
        UserEntity user = userJpaRepository.findById(order.userId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado para el pedido"));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(user);
        orderEntity.setPaymentMethod(order.paymentMethod());
        orderEntity.setStatus(order.status());
        orderEntity.setNotes(order.notes());
        orderEntity.setDeliveryMethod(order.deliveryMethod());
        orderEntity.setTotal(order.total());
        orderEntity.setCreatedAt(OffsetDateTime.now());

        for (OrderItemToSave item : order.items()) {
            ProductEntity product = productJpaRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado para item de pedido"));

            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setProduct(product);
            itemEntity.setProductName(item.productName());
            itemEntity.setVariantId(item.variantId());
            itemEntity.setQuantity(item.quantity());
            itemEntity.setUnitPrice(item.unitPrice());
            itemEntity.setSubtotal(item.subtotal());
            orderEntity.addItem(itemEntity);
        }

        OrderEntity saved = orderJpaRepository.save(orderEntity);

        return new CreatedOrder(
                saved.getId(),
                saved.getStatus(),
                saved.getTotal(),
                saved.getCreatedAt(),
                saved.getPaymentMethod(),
                saved.getItems().stream().map(item -> new CreatedOrderItem(
                        item.getProduct().getId(),
                        item.getProductName(),
                        item.getVariantId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                )).toList()
        );
    }

    @Override
    public CustomerOrderPage findByUserId(Long userId, int page, int size) {
        Page<OrderEntity> ordersPage = orderJpaRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));

        return new CustomerOrderPage(
                ordersPage.getContent().stream().map(order -> new CustomerOrderSummary(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getStatus(),
                        order.getTotal(),
                        order.getPaymentMethod(),
                        order.getItems().stream().mapToInt(OrderItemEntity::getQuantity).sum()
                )).toList(),
                ordersPage.getNumber(),
                ordersPage.getSize(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages()
        );
    }

    @Override
    public Optional<CustomerOrderDetail> findDetailById(Long orderId) {
        return orderJpaRepository.findWithItemsById(orderId)
                .map(order -> {
                    BigDecimal subtotal = order.getItems().stream()
                            .map(OrderItemEntity::getSubtotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal discounts = subtotal.subtract(order.getTotal());

                    return new CustomerOrderDetail(
                            order.getId(),
                            order.getUser().getId(),
                            order.getCreatedAt(),
                            order.getStatus(),
                            order.getPaymentMethod(),
                            order.getTotal(),
                            subtotal,
                            discounts,
                            order.getNotes(),
                            order.getItems().stream().map(item -> new CustomerOrderItemDetail(
                                    item.getProduct().getId(),
                                    item.getProductName(),
                                    item.getUnitPrice(),
                                    item.getQuantity(),
                                    item.getSubtotal(),
                                    item.getProduct().getMainImageUrl()
                            )).toList()
                    );
                });
    }
}
