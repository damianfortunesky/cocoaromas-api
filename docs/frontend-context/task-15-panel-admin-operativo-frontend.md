# Task 16 — Panel administrativo completo (frontend)

## Estado inicial relevado

Se revisaron las pantallas admin existentes y su grado de completitud:

- `AdminLayout` ya existía con navegación lateral y sección activa.
- Productos: existía listado + ABM, pero sin endpoint de status dedicado ni categorías reales.
- Stock: existía pantalla conectada, pero con endpoints desalineados (`/admin/products/stock`) y update por `PUT`.
- Pedidos admin: existía listado, detalle y cambio de estado; se reforzó la confirmación antes de actualizar estado.
- Promociones: existía ABM conectado, pero categorías derivadas de productos en vez de endpoint real de categorías.

## Cambios implementados

### 1) Integración de endpoints reales

- Se alinearon endpoints frontend con el contrato backend:
  - categorías públicas: `/categories`
  - catálogo público: `/products`
  - stock admin: `/admin/stocks` y `/admin/stocks/{productId}`
  - estado de producto: `/admin/products/{id}/status`
  - estado de promoción: `/admin/promotions/{id}/status`

### 2) Categorías reales (sin mocks)

- Se creó módulo de categorías con repository + query hook (`useCategories`).
- Se integraron categorías reales en:
  - formulario admin de productos (select)
  - filtro de productos admin por categoría
  - formulario de promociones para promociones por categoría
  - filtros de categoría del catálogo público (se reemplazaron categorías hardcodeadas)

### 3) Admin Products más operativo

- Se agregó filtro por búsqueda y por categoría en listado admin.
- Se migró toggle activo/inactivo al endpoint dedicado `PATCH /admin/products/{id}/status`.
- Se mantuvo ABM completo con feedback de loading/error/success.

### 4) Admin Stock alineado a API

- Se cambiaron endpoints de stock a `/admin/stocks`.
- Se cambió actualización de stock a `PATCH`.
- Se agregó estado vacío explícito para operación diaria.

### 5) Admin Orders

- Se mantuvo flujo real de listado, detalle y cambio de estado.
- Se agregó confirmación explícita antes de aplicar cambio de estado.

### 6) Admin Promotions

- Se mantuvo ABM real de promociones.
- Se cambió endpoint de activación/desactivación a `/status`.
- Se conectaron categorías reales para promociones por categoría.
- Se agregó empty state cuando no hay promociones.

## Resultado

El panel admin queda más usable para operación diaria en productos, stock, pedidos y promociones, con categorías reales integradas y sin categorías mockeadas en las vistas principales involucradas.
