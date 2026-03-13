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
