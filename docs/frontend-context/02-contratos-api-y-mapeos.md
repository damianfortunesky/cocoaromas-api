# Frontend — Contratos API y mapeos

## Endpoints consumidos

### Público
- `POST /auth/login`
- `POST /auth/register`
- `GET /products`
- `GET /products/{id}`
- `GET /categories`

### Usuario autenticado
- `GET /auth/me`
- `GET/PUT /me/profile`
- `GET/POST/PUT/DELETE /me/addresses`
- `POST /orders`
- `GET /orders/me`

### Administración
- Productos: `GET/POST/PUT/DELETE/PATCH /admin/products`
- Categorías: `GET/POST/PUT/DELETE /admin/categories`
- Promociones: `GET/POST/PUT/DELETE/PATCH /admin/promotions`
- Stock: `GET /admin/stocks`, `GET/PATCH /admin/stocks/{productId}`
- Pedidos: `GET /admin/orders`, `GET /admin/orders/{id}`, `PATCH /admin/orders/{id}/status`

## Compatibilidad de mapeos (frontend robusto)
Se mantienen mapeos tolerantes para contratos camelCase y snake_case en módulos críticos:
- Productos/catálogo (`name` vs `product_name`, `stock` vs `stock_quantity`, `isActive` vs `is_active`).
- Categorías (`name` vs `category_name`, `displayOrder` vs `display_order`).
- Promociones (`name/scope/type` vs `promotion_name/promotion_type/discount_type`).
- Pedidos (`status` vs `status_order`, `discount` vs `discount_total`).
- Cuenta (`documentId` vs `dni`, direcciones legacy vs `street/street_number/state_name`).

## Criterio operativo
- El frontend prioriza contratos actuales y conserva compatibilidad hacia atrás para despliegues parciales.
