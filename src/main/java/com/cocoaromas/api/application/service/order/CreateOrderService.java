package com.cocoaromas.api.application.service.order;

import com.cocoaromas.api.application.port.in.order.CreateOrderUseCase;
import com.cocoaromas.api.application.port.out.order.LoadProductsForOrderPort;
import com.cocoaromas.api.application.port.out.order.SaveOrderPort;
import com.cocoaromas.api.domain.order.CreateOrderCommand;
import com.cocoaromas.api.domain.order.CreateOrderItem;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService implements CreateOrderUseCase {

    private static final TypeReference<List<VariantJson>> LIST_VARIANT_JSON = new TypeReference<>() {
    };

    private final LoadProductsForOrderPort loadProductsForOrderPort;
    private final SaveOrderPort saveOrderPort;
    private final ObjectMapper objectMapper;

    public CreateOrderService(
            LoadProductsForOrderPort loadProductsForOrderPort,
            SaveOrderPort saveOrderPort,
            ObjectMapper objectMapper
    ) {
        this.loadProductsForOrderPort = loadProductsForOrderPort;
        this.saveOrderPort = saveOrderPort;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public com.cocoaromas.api.domain.order.CreatedOrder createOrder(CreateOrderCommand command) {
        if (command.userId() == null) {
            throw new InvalidOrderException("Usuario no autenticado");
        }
        if (command.items() == null || command.items().isEmpty()) {
            throw new OrderValidationException("El pedido debe incluir al menos un ítem");
        }
        if (command.paymentMethod() == null) {
            throw new OrderValidationException("paymentMethod es requerido");
        }

        List<SaveOrderPort.OrderItemToSave> items = new ArrayList<>();
        Set<String> dedup = new LinkedHashSet<>();
        Map<Long, LoadProductsForOrderPort.ProductForOrder> productsById = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItem item : command.items()) {
            validateItem(item);
            String compositeKey = item.productId() + "::" + (item.variantId() == null ? "" : item.variantId());
            if (!dedup.add(compositeKey)) {
                throw new OrderValidationException("Hay ítems repetidos para el mismo producto/variante");
            }

            LoadProductsForOrderPort.ProductForOrder product = productsById.computeIfAbsent(
                    item.productId(),
                    productId -> loadProductsForOrderPort.findById(productId)
                            .orElseThrow(() -> new OrderItemNotFoundException(productId))
            );

            if (!product.visible()) {
                throw new OrderValidationException("Producto no disponible para compra: " + product.id());
            }

            validateVariantIfNeeded(item, product);

            BigDecimal unitPrice = product.price();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.quantity()));
            total = total.add(subtotal);

            items.add(new SaveOrderPort.OrderItemToSave(
                    product.id(),
                    product.name(),
                    item.variantId(),
                    item.quantity(),
                    unitPrice,
                    subtotal
            ));
        }

        OrderStatus initialStatus = resolveInitialStatus(command.paymentMethod());
        return saveOrderPort.save(new SaveOrderPort.OrderToSave(
                command.userId(),
                command.paymentMethod(),
                initialStatus,
                command.notes(),
                command.deliveryMethod(),
                total,
                items
        ));
    }

    private void validateItem(CreateOrderItem item) {
        if (item == null || item.productId() == null) {
            throw new OrderValidationException("Cada ítem debe incluir productId");
        }
        if (item.quantity() == null || item.quantity() < 1) {
            throw new OrderValidationException("La cantidad debe ser mayor o igual a 1");
        }
    }

    private void validateVariantIfNeeded(CreateOrderItem item, LoadProductsForOrderPort.ProductForOrder product) {
        if (!product.hasVariants()) {
            return;
        }

        if (item.variantId() == null || item.variantId().isBlank()) {
            throw new OrderValidationException("El producto " + product.id() + " requiere variantId");
        }

        List<VariantJson> variants = parseVariants(product.variantsJson());
        VariantJson variant = variants.stream()
                .filter(v -> item.variantId().equals(v.id))
                .findFirst()
                .orElseThrow(() -> new OrderValidationException("Variante inválida para producto " + product.id()));

        if (variant.available == null || !variant.available) {
            throw new OrderValidationException("La variante seleccionada no está disponible");
        }
    }

    private List<VariantJson> parseVariants(String variantsJson) {
        if (variantsJson == null || variantsJson.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(variantsJson, LIST_VARIANT_JSON);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private OrderStatus resolveInitialStatus(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.TRANSFER
                ? OrderStatus.ESPERANDO_PAGO
                : OrderStatus.PENDIENTE;
    }

    private static class VariantJson {
        public String id;
        public String name;
        public Map<String, String> attributes;
        public Integer stockQuantity;
        public Boolean available;
    }
}
