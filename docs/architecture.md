# Arquitectura

## Estilo
- Ports and Adapters / Hexagonal Architecture

## Gestión administrativa de stock (Task 8)
- **Entrypoint REST**: `AdminStockController` expone `/api/v1/admin/stocks`.
- **Puerto de entrada**: `AdminStocksUseCase`.
- **Servicio de aplicación**: `AdminStocksService` concentra validaciones (no stock negativo, payload consistente y estrategia de actualización por `PATCH`).
- **Puerto de salida**: `ManageAdminStocksPort`.
- **Adaptador de persistencia**: `AdminStockPersistenceAdapter` sobre `ProductJpaRepository`.
- **Modelo actual**: stock simple en `products.stock_quantity` con `has_variants` para evolucionar a stock por variante sin romper contratos.
- **Umbral low stock**: configurable por `app.stock.low-threshold` (default `5`).

## Gestión administrativa de pedidos (Task 9)
- **Entrypoint REST**: `AdminOrderController` expone `/api/v1/admin/orders`.
- **Puerto de entrada**: `AdminOrdersUseCase`.
- **Servicio de aplicación**: `AdminOrdersService` valida transiciones de estado y dispara un hook cuando pasa a `PAGADO`.
- **Puerto de salida**: `ManageAdminOrdersPort`.
- **Adaptador de persistencia**: `AdminOrderPersistenceAdapter` sobre `OrderJpaRepository` con filtros administrativos.
- **Política de estados**: `OrderStatusTransitionPolicy` centraliza transiciones permitidas.
- **Preparación pagos/stock**: `OrderPaidHookPort` + `NoopOrderPaidHookAdapter` dejan un punto de extensión para integrar Mercado Pago y descuento de stock al pasar a pagado sin refactor mayor.
- **Seguridad de pedidos admin**: endpoints restringidos con `@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")`.
