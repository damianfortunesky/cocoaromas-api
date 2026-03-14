# Task 08 - Gestión administrativa de stock

## Objetivo
Proveer al admin herramientas para ajustar inventario y mantener disponibilidad correcta.

## Alcance implementado
- Vista administrativa de stock por producto.
- Actualización de cantidades con validaciones de negocio.
- Historial básico o feedback de ajustes realizados.
- Integración con alertas de éxito/error.

## Contexto para backend
- Consumo de endpoints de stock protegidos para admin.
- Frontend muestra rechazos de reglas (negativos, inconsistencias, etc.).

## Criterios de aceptación
- Admin puede incrementar/disminuir stock con trazabilidad mínima.
- Operaciones inválidas no impactan inventario.
- Cambios impactan disponibilidad de catálogo.

## Trazabilidad
- Tipo: operación e inventario.
- Dependencias: Task 05, Task 06, Task 07.
- Desbloquea: control diario de disponibilidad comercial.
