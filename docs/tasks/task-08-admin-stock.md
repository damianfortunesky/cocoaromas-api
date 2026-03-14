# Task 08 - Gestión administrativa de stock

## Objetivo
Implementar módulo admin de stock con listado, detalle y actualización segura.

## Alcance implementado
- `GET /api/v1/admin/stocks`.
- `GET /api/v1/admin/stocks/{productId}`.
- `PATCH /api/v1/admin/stocks/{productId}`.
- Validaciones de no stock negativo y modo de actualización.
- Umbral configurable de low stock (`app.stock.low-threshold`).

## Contexto para frontend
- Permite vista operacional de stock con alertas de bajo inventario.
- Contrato estable para futuras variantes sin romper endpoints actuales.

## Criterios de aceptación
- Listado paginado/filtrado para panel.
- Actualización de stock con validaciones de negocio.
- Seguridad por roles habilitados.

## Trazabilidad
- Tipo: módulo admin operativo.
- Dependencias: Task 05 y Task 07.
