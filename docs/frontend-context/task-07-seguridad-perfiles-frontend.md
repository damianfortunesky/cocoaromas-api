# Task 07 - Seguridad de rutas y perfiles

## Objetivo
Endurecer control de acceso por perfil y proteger vistas/acciones según rol.

## Alcance implementado
- Guards de rutas públicas, privadas y administrativas.
- Render condicional de acciones sensibles por perfil.
- Manejo de sesión expirada y redirecciones seguras.
- Protección contra acceso directo por URL a pantallas no autorizadas.

## Contexto para backend
- Frontend interpreta claims/perfiles retornados por autenticación.
- Coordinación con códigos `401` y `403` para UX consistente.

## Criterios de aceptación
- Cliente no puede acceder a rutas admin.
- Admin conserva acceso completo a módulos permitidos.
- Sesión inválida deriva a login sin loops.

## Trazabilidad
- Tipo: seguridad de aplicación.
- Dependencias: Task 02.
- Desbloquea: módulos admin productivos con menor riesgo.
