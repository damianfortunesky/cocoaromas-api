# Task 05 - Admin de productos

## Objetivo
Habilitar ABM administrativo de productos para alimentar catálogo y operación diaria.

## Alcance implementado
- `POST /api/v1/admin/products`.
- `PUT /api/v1/admin/products/{id}`.
- `DELETE /api/v1/admin/products/{id}`.
- `PATCH /api/v1/admin/products/{id}/status`.
- Campos administrativos y compatibilidad con datos legacy JSON.

## Contexto para frontend
- El panel admin puede editar disponibilidad sin romper consumo público.
- Se contempla evolución de atributos de producto sin modelo rígido.

## Criterios de aceptación
- Endpoints protegidos por rol administrativo.
- Validaciones de payload en creación/edición.
- Eliminación/estado respetan consistencia de catálogo.

## Trazabilidad
- Tipo: administración de catálogo.
- Dependencias: Task 03.
