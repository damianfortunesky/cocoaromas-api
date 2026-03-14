# Task 05 - Panel admin de productos

## Objetivo
Habilitar interfaz administrativa para alta, edición y gestión del catálogo de productos.

## Alcance implementado
- Listado administrativo de productos.
- Formularios de crear/editar producto.
- Acciones de activación/inactivación según reglas de negocio.
- Mensajes de éxito/error para operaciones CRUD.

## Contexto para backend
- Requiere endpoints administrativos protegidos por rol.
- UI refleja validaciones de campos y reglas de integridad.

## Criterios de aceptación
- Admin autenticado puede crear y editar productos.
- Usuario no admin no accede al módulo.
- Cambios se reflejan en catálogo tras persistencia.

## Trazabilidad
- Tipo: operación comercial.
- Dependencias: Task 02, Task 03.
- Desbloquea: gestión de stock/promociones sobre productos.
