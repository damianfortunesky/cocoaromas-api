# Task 17 - Backend adaptación a modelo de datos simplificado

## Objetivo
Alinear la API REST con el esquema base definido en `V1__initial_schema.sql`, asegurando que los mapeos JPA reflejen correctamente los nombres de columna actuales y evitando errores de ejecución por desajustes ORM ↔ DB.

## Cambios implementados

### 1) Ajuste de mapeos de columnas en entidades JPA
Se actualizaron anotaciones `@Column` para coincidir con el esquema de Flyway:

- `users.role_name` en lugar de `role`
- `categories.category_name` en lugar de `name`
- `products.product_name` en lugar de `name`
- `products.product_description` en lugar de `description`
- `orders.status_order` en lugar de `status`
- `promotions.promotion_name` en lugar de `name`
- `promotions.promotion_description` en lugar de `description`
- `user_addresses.state_name` en lugar de `state`

### 2) Alcance respetado
Se mantuvieron sin eliminar los módulos de pedidos/pagos, tal como se indicó. Solo se ajustó el mapeo para mantener compatibilidad con el esquema activo.

## Nota de validación
Se intentó compilar con Maven, pero la validación está bloqueada por acceso denegado al repositorio central (`403`), por lo que no fue posible verificar compilación completa en este entorno.
