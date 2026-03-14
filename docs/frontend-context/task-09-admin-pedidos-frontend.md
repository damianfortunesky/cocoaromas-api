# Task 09 - Gestión administrativa de pedidos

## Objetivo
Permitir al equipo administrativo visualizar y operar el ciclo de vida de pedidos.

## Alcance implementado
- Listado de pedidos con filtros de estado.
- Vista de detalle de pedido para revisión operativa.
- Acciones de cambio de estado según flujo permitido.
- Feedback visual para confirmación/rechazo de acciones.

## Contexto para backend
- Requiere endpoints de consulta y transición de estado.
- La UI se adapta a reglas de workflow definidas por negocio.

## Criterios de aceptación
- Admin puede listar pedidos y actualizar estados válidos.
- No se muestran acciones incompatibles con el estado actual.
- Errores de transición se comunican claramente.

## Trazabilidad
- Tipo: operación post-venta.
- Dependencias: Task 04, Task 07.
- Desbloquea: gestión operativa completa de órdenes.
