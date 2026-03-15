package com.cocoaromas.api.application.service.order;

import com.cocoaromas.api.application.port.in.order.CreateOrderUseCase;
import com.cocoaromas.api.application.port.out.order.LoadProductsForOrderPort;
import com.cocoaromas.api.application.port.out.order.SaveOrderPort;
import com.cocoaromas.api.domain.order.CreateOrderCommand;
import com.cocoaromas.api.domain.order.CreateOrderItem;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService implements CreateOrderUseCase {

    private final LoadProductsForOrderPort loadProductsForOrderPort;
    private final SaveOrderPort saveOrderPort;

    public CreateOrderService(
            LoadProductsForOrderPort loadProductsForOrderPort,
            SaveOrderPort saveOrderPort
    ) {
        this.loadProductsForOrderPort = loadProductsForOrderPort;
        this.saveOrderPort = saveOrderPort;
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
            String compositeKey = String.valueOf(item.productId());
            if (!dedup.add(compositeKey)) {
                throw new OrderValidationException("Hay ítems repetidos para el mismo producto");
            }

            LoadProductsForOrderPort.ProductForOrder product = productsById.computeIfAbsent(
                    item.productId(),
                    productId -> loadProductsForOrderPort.findById(productId)
                            .orElseThrow(() -> new OrderItemNotFoundException(productId))
            );

            if (!product.active()) {
                throw new OrderValidationException("Producto no disponible para compra: " + product.id());
            }

            BigDecimal unitPrice = product.price();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.quantity()));
            total = total.add(subtotal);

            items.add(new SaveOrderPort.OrderItemToSave(
                    product.id(),
                    product.name(),
                    null,
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

    private OrderStatus resolveInitialStatus(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.TRANSFER
                ? OrderStatus.ESPERANDO_PAGO
                : OrderStatus.PENDIENTE;
    }
}
