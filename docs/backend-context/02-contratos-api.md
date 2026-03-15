# Contratos API consolidados

## 1) Autenticación y usuario actual

- `POST /api/v1/auth/login`
- `GET /api/v1/auth/me`
- `POST /api/v1/auth/register` (público; crea usuario con rol `CLIENT`)

### Notas
- Autenticación con JWT Bearer token.
- No hay refresh token en esta etapa.
- `register` valida duplicados de email/username y no permite asignar rol desde frontend.

## 2) Catálogo público

- `GET /api/v1/products` (listado paginado con filtros)
- `GET /api/v1/products/{id}`
- `GET /api/v1/categories`

### Notas
- Productos agotados permanecen visibles.
- Contrato preparado para consumo simple desde home/listado/detalle.

## 3) Pedidos cliente

- `POST /api/v1/orders`
- `GET /api/v1/orders/me`
- `GET /api/v1/orders/{id}`

### Notas
- Requiere autenticación.
- El usuario solo puede consultar sus propios pedidos.

## 4) Perfil y direcciones de usuario autenticado

- `GET /api/v1/me/profile`
- `PUT /api/v1/me/profile`
- `GET /api/v1/me/addresses`
- `POST /api/v1/me/addresses`
- `PUT /api/v1/me/addresses/{addressId}`
- `DELETE /api/v1/me/addresses/{addressId}` (soft delete)

### Notas
- Upsert de `user_details` por usuario.
- Manejo de dirección default shipping/billing.

## 5) Administración de productos

- `POST /api/v1/admin/products`
- `PUT /api/v1/admin/products/{id}`
- `DELETE /api/v1/admin/products/{id}`
- `PATCH /api/v1/admin/products/{id}/status`

## 6) Administración de stock

- `GET /api/v1/admin/stocks`
- `GET /api/v1/admin/stocks/{productId}`
- `PATCH /api/v1/admin/stocks/{productId}`

### Notas
- Incluye validación de stock no negativo.
- Umbral configurable de low stock: `app.stock.low-threshold`.
- Actualizaciones administrativas registran trazabilidad en `inventory_movements`.

## 7) Administración de pedidos

- `GET /api/v1/admin/orders`
- `GET /api/v1/admin/orders/{orderId}`
- `PATCH /api/v1/admin/orders/{orderId}/status`

### Notas
- Usa política de transición de estados centralizada.
- Existe hook extensible al estado `PAGADO`.

## 8) Administración de promociones

- `GET /api/v1/admin/promotions`
- `POST /api/v1/admin/promotions`
- `PUT /api/v1/admin/promotions/{id}`
- `PATCH /api/v1/admin/promotions/{id}/status`
- `DELETE /api/v1/admin/promotions/{id}` (soft delete)

### Notas
- Reglas por tipo: `QUANTITY`, `PRODUCT`, `CATEGORY`.
- Tipos de descuento: `PERCENTAGE`, `FIXED_AMOUNT`.

## 9) Administración de categorías

- `GET /api/v1/admin/categories`
- `POST /api/v1/admin/categories`
- `PUT /api/v1/admin/categories/{id}`
- `DELETE /api/v1/admin/categories/{id}`

## Endpoints técnicos complementarios

- `GET /api/v1/ping`
- OpenAPI/Swagger
- Actuator/Prometheus
