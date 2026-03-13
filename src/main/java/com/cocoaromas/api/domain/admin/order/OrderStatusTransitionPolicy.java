package com.cocoaromas.api.domain.admin.order;

import com.cocoaromas.api.domain.order.OrderStatus;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class OrderStatusTransitionPolicy {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new EnumMap<>(OrderStatus.class);

    static {
        ALLOWED.put(OrderStatus.PENDIENTE, EnumSet.of(OrderStatus.ESPERANDO_PAGO, OrderStatus.CANCELADO));
        ALLOWED.put(OrderStatus.ESPERANDO_PAGO, EnumSet.of(OrderStatus.PAGADO, OrderStatus.CANCELADO));
        ALLOWED.put(OrderStatus.PAGADO, EnumSet.of(OrderStatus.PREPARANDO));
        ALLOWED.put(OrderStatus.PREPARANDO, EnumSet.of(OrderStatus.ENVIADO));
        ALLOWED.put(OrderStatus.ENVIADO, EnumSet.of(OrderStatus.ENTREGADO));
        ALLOWED.put(OrderStatus.ENTREGADO, EnumSet.noneOf(OrderStatus.class));
        ALLOWED.put(OrderStatus.CANCELADO, EnumSet.noneOf(OrderStatus.class));
    }

    private OrderStatusTransitionPolicy() {
    }

    public static boolean isAllowed(OrderStatus from, OrderStatus to) {
        return ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }

    public static Set<OrderStatus> allowedTargets(OrderStatus from) {
        return ALLOWED.getOrDefault(from, Set.of());
    }
}
