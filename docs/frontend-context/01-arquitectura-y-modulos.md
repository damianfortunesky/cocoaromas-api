# Frontend — Arquitectura y módulos

## Stack y base técnica
- SPA en **React + Vite + TypeScript**.
- Routing por layouts (`PublicLayout`, `AdminLayout`) con guards por sesión/rol.
- Capa HTTP centralizada y manejo unificado de errores.
- React Query para estado remoto y sincronización de mutaciones.

## Organización por dominios
- `auth`: login, registro, sesión y roles.
- `catalog`: catálogo público y detalle de producto.
- `cart`: estado local de carrito + reglas de selección.
- `orders`: checkout cliente, historial y gestión admin.
- `products`, `categories`, `stock`, `promotions`: operación administrativa.
- `account`: perfil y direcciones del usuario autenticado.

## Decisiones de diseño consolidadas
- Componentes reutilizables en `src/shared/ui` para uniformidad visual.
- Formularios con validación explícita y feedback en éxito/error.
- Estados obligatorios en vistas: `loading`, `error`, `empty`, `success`.
- Navegación y permisos guiados por rol (`admin`, `employee`, `owner`, `client`).

## Historial resumido de evolución
El frontend consolidó los incrementos de las tasks históricas: bootstrap base, auth JWT, catálogo público, carrito/checkout, paneles admin, seguridad por perfiles, registro, mejoras responsive y correcciones de bugs de sesión/login.
