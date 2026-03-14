# Task 03 - Catálogo público (productos, detalle, categorías)

## Objetivo
Publicar catálogo para home/listado/detalle con filtros y ordenamiento consumibles por frontend.

## Alcance implementado
- `GET /api/v1/products` con paginación/filtros básicos.
- `GET /api/v1/products/{id}` para detalle público.
- `GET /api/v1/categories` para navegación por categorías.
- Soporte de stock visible sin ocultar productos agotados.

## Contexto para frontend
- Productos sin stock deben mostrarse visibles pero no comprables.
- El detalle mantiene estructura apta para atributos flexibles/variantes.

## Criterios de aceptación
- Lista paginada consistente.
- Detalle inexistente retorna 404 controlado.
- Categorías disponibles para filtros de UI.

## Trazabilidad
- Tipo: contrato público de ecommerce.
- Dependencias: Task 01.
