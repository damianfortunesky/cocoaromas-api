# 06 - Modelo de datos vigente

Este documento resume las tablas actualmente definidas en el esquema SQL principal del backend.

## 1) `users`

Almacena cuentas de acceso.

- PK: `id`
- Campos clave: `email`, `password_hash`, `role_name`, `is_active`, `created_at`, `updated_at`
- Restricciones:
  - `uq_users_email` (email único)

## 2) `categories`

Catálogo de categorías de productos.

- PK: `id`
- Campos clave: `category_name`, `slug`, `display_order`
- Restricciones:
  - `uq_categories_name`
  - `uq_categories_slug`

## 3) `products`

Productos comercializables.

- PK: `id`
- FK: `category_id -> categories(id)`
- Campos clave: `product_name`, `product_description`, `price`, `stock_quantity`, `image_url`, `is_active`, `deleted_at`, `created_at`, `updated_at`
- Checks:
  - `stock_quantity >= 0`
  - `price >= 0`

## 4) `user_details`

Perfil extendido por usuario (1 a 1).

- PK: `id`
- FK: `user_id -> users(id)`
- Campos clave: `first_name`, `last_name`, `phone`, `dni`, `birth_date`
- Restricciones:
  - `uq_user_details_user` (1 registro por usuario)

## 5) `user_addresses`

Direcciones del usuario autenticado.

- PK: `id`
- FK: `user_id -> users(id)`
- Campos clave:
  - `label`, `receiver_name`, `receiver_phone`
  - `street`, `street_number`, `floor`, `apartment`
  - `city`, `state_name`, `postal_code`, `country_code`, `reference`
  - `is_default_shipping`, `is_default_billing`, `is_active`
  - `created_at`, `updated_at`
- Checks:
  - `LEN(country_code) = 2`

## 6) `orders`

Cabecera de pedidos.

- PK: `id`
- FK: `user_id -> users(id)`
- Campos clave:
  - `status_order`, `payment_method`, `payment_status`
  - `notes`, `delivery_method`
  - `subtotal`, `discount_total`, `shipping_total`, `total`, `currency_code`
  - `status_reason`, `created_at`, `updated_at`
- Checks:
  - `LEN(currency_code) = 3`
  - montos no negativos

## 7) `order_items`

Detalle de ítems por pedido.

- PK: `id`
- FK:
  - `order_id -> orders(id)`
  - `product_id -> products(id)`
- Campos clave:
  - `product_name`, `variant_id`, `product_variant_id`
  - `quantity`, `unit_price`, `subtotal`, `discount_amount`, `tax_amount`, `total_amount`
- Checks:
  - `quantity > 0`
  - montos no negativos

## 8) `promotions`

Promociones configurables para producto o categoría.

- PK: `id`
- FK:
  - `product_id -> products(id)`
  - `category_id -> categories(id)`
- Campos clave:
  - `promotion_name`, `promotion_description`
  - `promotion_type`, `discount_type`, `discount_value`
  - `minimum_quantity`, `is_active`
  - `starts_at`, `ends_at`, `deleted_at`, `created_at`, `updated_at`
- Checks:
  - `discount_value >= 0`

## 9) `inventory_movements`

Auditoría de movimientos de stock.

- PK: `id`
- FK:
  - `product_id -> products(id)`
  - `created_by_user_id -> users(id)`
- Campos clave:
  - `movement_type`, `quantity`, `reference_type`, `notes`, `created_at`
- Checks:
  - `quantity <> 0`

## 10) Índices relevantes

- `products`: categoría, activo, borrado lógico.
- `orders`: usuario, estado, fecha creación, método y estado de pago.
- `order_items`: por pedido y por producto.
- `user_addresses`: por usuario y defaults.
- `user_details`: por usuario.
- `promotions`: activo, tipo, vigencia y borrado lógico.
- `inventory_movements`: por producto y por usuario creador.
