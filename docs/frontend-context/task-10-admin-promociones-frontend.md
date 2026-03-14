# Task 10 - Gestión administrativa de promociones

## Objetivo
Habilitar creación y mantenimiento de promociones desde panel administrativo.

## Alcance implementado
- Listado admin de promociones vigentes/históricas.
- Formularios para crear y editar promociones.
- Reglas de vigencia y elegibilidad reflejadas en UI.
- Integración con catálogo para mostrar impacto en precio.

## Contexto para backend
- Frontend consume endpoints de promociones con validaciones temporales.
- Requiere consistencia entre precio base, descuento y precio final.

## Criterios de aceptación
- Admin puede gestionar promociones válidas.
- Promociones inválidas muestran errores de regla de negocio.
- Catálogo refleja precio promocional correctamente.

## Trazabilidad
- Tipo: marketing y conversión.
- Dependencias: Task 05, Task 07.
- Desbloquea: campañas comerciales autogestionadas.
