# Task 13 - Backend flows mínimos viables

## Objetivo
Completar los flujos mínimos para operar la app en:
- información de usuarios (`user_details`, `user_addresses`)
- productos
- promociones
- categorías
- gestión de stock completa

## Alcance implementado

### 1) Perfil y direcciones de usuario autenticado
Se agregaron endpoints para que el usuario autenticado pueda completar y mantener sus datos:
- `GET /api/v1/me/profile`
- `PUT /api/v1/me/profile`
- `GET /api/v1/me/addresses`
- `POST /api/v1/me/addresses`
- `PUT /api/v1/me/addresses/{addressId}`
- `DELETE /api/v1/me/addresses/{addressId}` (soft delete)

Reglas incluidas:
- upsert de `user_details` por usuario
- validaciones mínimas de campos obligatorios
- manejo de dirección default shipping/billing por usuario

### 2) Categorías administrativas
Se agregó CRUD administrativo de categorías:
- `GET /api/v1/admin/categories`
- `POST /api/v1/admin/categories`
- `PUT /api/v1/admin/categories/{id}`
- `DELETE /api/v1/admin/categories/{id}`

Permisos: `ADMIN`, `OWNER`, `EMPLOYEE`.

### 3) Stock completo con trazabilidad
Se extendió actualización de stock admin para registrar movimientos en `inventory_movements`:
- movimiento `IN` o `OUT`
- referencia `ADMIN_MANUAL`
- cantidad ajustada
- motivo (`reason`)
- usuario actor (si existe en contexto de seguridad)

Esto complementa el flujo de stock ya existente en `/api/v1/admin/stocks` con auditoría operativa.

### 4) Productos y promociones
Se preservan y mantienen operativos los módulos existentes:
- productos admin (`/api/v1/admin/products`)
- promociones admin (`/api/v1/admin/promotions`)
- catálogo público (`/api/v1/products`, `/api/v1/categories`)

No se incluyó módulo de pedidos/pagos en esta tarea (según observación de alcance).

## Notas
- Se mantuvo consistencia con el esquema SQL Server ya migrado (tablas `user_details`, `user_addresses`, `inventory_movements`, `categories`).
- Se intentó compilación Maven, bloqueada por restricción de red hacia Maven Central (403).
