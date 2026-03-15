# Modelo de datos y evolución

## Simplificación del modelo de productos

Se consolidó el dominio de productos hacia una estructura plana:

- `id`
- `name`
- `description`
- `price`
- `categoryId`
- `stockQuantity`
- `imageUrl`
- `isActive`
- `createdAt`
- `updatedAt`

## Cambios eliminados del modelo anterior

Se retiraron elementos complejos/legacy del flujo principal:

- variantes de producto
- atributos dinámicos
- short/long description separadas
- múltiples campos/listas JSON de imágenes
- flags redundantes de visibilidad/disponibilidad

## Persistencia y migraciones

Se incorporó migración `V14__simplify_products_model.sql` para converger esquema:

- `description` único
- `image_url` nullable
- `stock_quantity` como fuente única de stock
- `is_active` como estado único de publicación

Columnas legacy removidas:

- `short_description`
- `long_description`
- `main_image_url`
- `image_urls_json`
- `attributes_json`
- `variants_json`
- `has_variants`
- `is_visible`
- `is_available`

## Compatibilidad ORM ↔ DB (alineación de mapeos)

Se ajustaron entidades JPA para coincidir con nombres de columnas reales del esquema:

- `users.role_name`
- `categories.category_name`
- `products.product_name`
- `products.product_description`
- `orders.status_order`
- `promotions.promotion_name`
- `promotions.promotion_description`
- `user_addresses.state_name`

## Notas operativas del entorno

- En intentos de validación/compilación, Maven tuvo bloqueos por acceso a Maven Central (`403`) en este entorno.
- La documentación consolidada mantiene foco en contratos y reglas que sí están implementadas en el backend.
