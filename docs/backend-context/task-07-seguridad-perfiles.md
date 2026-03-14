# Task 07 - Endurecimiento de seguridad y perfiles

## Objetivo
Consolidar permisos por rol y comportamiento por endpoint para panel/admin/cliente.

## Alcance implementado
- Restricciones por rol para endpoints administrativos.
- Definición de permisos clave:
  - `admin`: acceso total.
  - `owner/admin/employee`: actualización de stock.
  - `admin/employee`: cambio de estado de pedidos.
  - gestión de roles limitada a `admin`.
- Estandarización de errores de autenticación/autorización.

## Contexto para frontend
- Permite construir guards/rutas por rol con una matriz clara de permisos.
- Evita ambigüedad entre módulos de panel.

## Criterios de aceptación
- Endpoints deniegan acceso cuando el rol no corresponde.
- Errores de seguridad devuelven respuestas consistentes.

## Trazabilidad
- Tipo: seguridad funcional.
- Dependencias: Task 02.
