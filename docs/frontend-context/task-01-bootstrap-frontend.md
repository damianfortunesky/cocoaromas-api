# Task 01 - Bootstrap frontend y arquitectura base

## Objetivo
Crear la base de la SPA con React + Vite, estructura modular y convenciones para escalar por dominios.

## Alcance implementado
- Setup de proyecto, routing principal y layouts base.
- Estructura por módulos (`auth`, `catalog`, `cart`, `orders`, `admin`, etc.).
- Configuración de cliente HTTP y variables de entorno.
- Base de estilos globales y componentes compartidos.

## Contexto para backend
- Frontend espera API bajo `/api/v1`.
- Manejo centralizado de errores HTTP para mapear códigos de negocio.

## Criterios de aceptación
- App corre localmente con `npm run dev`.
- Rutas iniciales renderizan sin errores.
- Integración HTTP preparada para siguientes tareas.

## Trazabilidad
- Tipo: infraestructura fundacional.
- Dependencias: ninguna.
- Desbloquea: auth, catálogo, carrito, pedidos y módulos admin.
