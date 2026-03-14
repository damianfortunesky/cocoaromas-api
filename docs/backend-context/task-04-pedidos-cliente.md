# Task 04 - Checkout y pedidos de cliente

## Objetivo
Permitir al usuario autenticado crear pedidos y consultar su historial/detalle.

## Alcance implementado
- `POST /api/v1/orders` para creación de pedido.
- `GET /api/v1/orders/me` para listado propio.
- `GET /api/v1/orders/{id}` para detalle de pedido del cliente.
- Estados iniciales de pedido alineados a reglas del negocio.

## Contexto para frontend
- Requiere usuario logueado para comprar.
- El flujo de checkout debe considerar que el descuento de stock no ocurre al crear pedido.

## Criterios de aceptación
- Creación de pedido valida items/productos.
- Usuario sólo ve sus propios pedidos.
- Detalle refleja items y totales esperados.

## Trazabilidad
- Tipo: núcleo de checkout.
- Dependencias: Task 02 y Task 03.
