# Task 15 - Simplificación del modelo de productos

## Resumen
Se simplificó el modelo de productos para operar con una estructura plana y mínima:

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

## Backend

### CRUD administrativo
`/api/v1/admin/products` ahora usa el contrato simplificado tanto en request como response.

### Modelo de dominio
Se eliminaron del flujo administrativo y de catálogo:

- variantes de producto
- atributos dinámicos
- descripción corta/larga separadas
- múltiples listas/JSON de imágenes
- flags redundantes de visibilidad/disponibilidad

### Persistencia
Se incorporó migración `V14__simplify_products_model.sql` para converger el esquema a:

- `description` único
- `image_url` nullable
- `stock_quantity` como fuente única de stock
- `is_active` como estado único de publicación

Y se eliminaron columnas legacy del modelo anterior (`short_description`, `long_description`, `main_image_url`, `image_urls_json`, `attributes_json`, `variants_json`, `has_variants`, `is_visible`, `is_available`).
