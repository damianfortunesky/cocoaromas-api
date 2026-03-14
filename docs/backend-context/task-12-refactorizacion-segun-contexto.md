# Task 12 - Refactorización según contexto

## Objetivo
- Validar consistencia backend/frontend priorizando reglas de backend.
- Refactorizar donde aporte a funcionamiento y performance.
- Asegurar operatividad de features ya implementadas.

## Cambios realizados

### 1) Refactor en creación de pedidos para reducir accesos redundantes
En `CreateOrderService` se incorporó un cache en memoria por request (`Map<Long, ProductForOrder> productsById`) para evitar múltiples llamadas a persistencia cuando en un mismo pedido se repite el `productId` con variantes diferentes.

**Motivación funcional y técnica:**
- En frontend es válido elegir distintas variantes del mismo producto en un mismo checkout.
- El servicio validaba deduplicación por `productId + variantId` (correcto), pero consultaba producto por cada ítem.
- Con el cache por `productId`, se mantiene el comportamiento funcional y se mejora eficiencia en pedidos con varias variantes.

### 2) Cobertura de prueba unitaria para el escenario optimizado
Se agregó test en `CreateOrderServiceTest` que valida:
- Pedido con dos ítems del mismo producto y variantes distintas.
- Verificación de que `loadProductsForOrderPort.findById(1L)` se invoca una sola vez.

## Reglas preservadas
- Continúa el bloqueo de ítems duplicados para misma combinación producto/variante.
- Continúan validaciones de visibilidad de producto y disponibilidad de variante.
- No se altera cálculo de subtotal/total ni estado inicial por método de pago.

## Notas de validación
- Se intentó ejecutar la suite de tests de Maven, pero el entorno respondió `403 Forbidden` al resolver el parent POM de Spring Boot desde Maven Central.
- El cambio se validó mediante análisis de flujo y prueba unitaria agregada.

## Impacto esperado
- Menor cantidad de consultas repetidas en checkout para carritos con varias variantes del mismo producto.
- Mejora de performance sin cambios de contrato API ni de reglas de negocio.
