# Task 09 - Gestión administrativa de pedidos

## Objetivo
Permitir operación administrativa de pedidos con control de transiciones de estado.

## Alcance implementado
- `GET /api/v1/admin/orders`.
- `GET /api/v1/admin/orders/{orderId}`.
- `PATCH /api/v1/admin/orders/{orderId}/status`.
- Política centralizada de transiciones (`OrderStatusTransitionPolicy`).
- Hook extensible al pasar a `PAGADO` para integrar stock/pagos.

## Contexto para frontend
- El panel puede reflejar workflow real del pedido (pendiente → entregado/cancelado).
- Permite auditar estados inválidos desde UX con mensajes claros.

## Criterios de aceptación
- Transiciones inválidas son rechazadas.
- Permisos por rol aplicados (`admin`, `employee`).
- Cambio a pagado dispara punto de extensión técnico.

## Trazabilidad
- Tipo: operación logística/comercial.
- Dependencias: Task 04 y Task 07.
