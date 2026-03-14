# Task 06 - Reglas de stock en interacción de compra

## Objetivo
Alinear el comportamiento del frontend con las reglas de stock para evitar operaciones inválidas.

## Alcance implementado
- Bloqueo de cantidades fuera de disponibilidad.
- Mensajes preventivos en carrito/checkout cuando no hay stock.
- Sincronización de stock en vistas clave (catálogo, detalle, carrito).
- Manejo de conflictos de stock entre consulta y confirmación.

## Contexto para backend
- Frontend depende de respuestas de validación de stock en tiempo de pedido.
- Se prioriza consistencia de negocio ante cambios concurrentes.

## Criterios de aceptación
- No se puede confirmar compra sin stock suficiente.
- Cambios de stock se comunican claramente al usuario.
- La UI evita estados ambiguos en cantidades.

## Trazabilidad
- Tipo: integridad transaccional.
- Dependencias: Task 03, Task 04.
- Desbloquea: operación estable de pedidos en producción.
