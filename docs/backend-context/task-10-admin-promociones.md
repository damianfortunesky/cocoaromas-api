# Task 10 - Gestión administrativa de promociones

## Objetivo
Habilitar CRUD de promociones con reglas por tipo y vigencia.

## Alcance implementado
- `GET /api/v1/admin/promotions`.
- `POST /api/v1/admin/promotions`.
- `PUT /api/v1/admin/promotions/{id}`.
- `PATCH /api/v1/admin/promotions/{id}/status`.
- `DELETE /api/v1/admin/promotions/{id}` (soft delete).
- Validaciones por tipo (`QUANTITY`, `PRODUCT`, `CATEGORY`) y descuento (`PERCENTAGE`, `FIXED_AMOUNT`).

## Contexto para frontend
- El panel admin puede crear reglas comerciales sin tocar código.
- Preserva historial al usar borrado lógico.

## Criterios de aceptación
- Payload consistente según tipo de promo.
- Vigencia validada (fechas coherentes).
- Endpoints restringidos a `admin`.

## Trazabilidad
- Tipo: pricing/comercial.
- Dependencias: Task 07.
