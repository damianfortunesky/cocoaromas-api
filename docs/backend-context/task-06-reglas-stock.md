# Task 06 - Reglas de stock y no descuento en creación

## Objetivo
Alinear la lógica de stock con negocio: visibilidad de agotados y descuento diferido al pago.

## Alcance implementado
- Reglas explícitas para no descontar stock al crear pedido.
- Preparación del modelo para stock simple y futura variante.
- Contratos y comportamiento para marcar disponibilidad en catálogo.

## Contexto para frontend
- Debe poder mostrar "sin stock" sin ocultar producto.
- Mensajería de checkout debe aclarar estado de pago/confirmación.

## Criterios de aceptación
- No hay decremento de stock en `POST /orders`.
- El producto agotado continúa visible.
- Base lista para hook de descuento al evento pagado.

## Trazabilidad
- Tipo: regla transversal de negocio.
- Dependencias: Task 04.
