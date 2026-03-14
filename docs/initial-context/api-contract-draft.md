# API Contract Draft

## Auth
POST /api/v1/auth/register
POST /api/v1/auth/login
GET /api/v1/auth/me

## Catálogo
GET /api/v1/products
GET /api/v1/products/{id}
GET /api/v1/categories

## Pedidos cliente
POST /api/v1/orders
GET /api/v1/orders/me
GET /api/v1/orders/{id}

## Admin productos
POST /api/v1/admin/products
PUT /api/v1/admin/products/{id}
DELETE /api/v1/admin/products/{id}
PATCH /api/v1/admin/products/{id}/status

## Stock
GET /api/v1/admin/stocks
GET /api/v1/admin/stocks/{productId}
PATCH /api/v1/admin/stocks/{productId}

## Pedidos admin
GET /api/v1/admin/orders
GET /api/v1/admin/orders/{orderId}
PATCH /api/v1/admin/orders/{orderId}/status

## Promociones
GET /api/v1/admin/promotions
POST /api/v1/admin/promotions
PUT /api/v1/admin/promotions/{id}
PATCH /api/v1/admin/promotions/{id}/status
DELETE /api/v1/admin/promotions/{id}
