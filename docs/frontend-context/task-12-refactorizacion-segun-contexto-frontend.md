# Task 12 - Refactorización según contexto

## Objetivo
Alinear el frontend con el contexto funcional actual del backend y eliminar dependencias de datos simulados, asegurando funcionamiento consistente, mejor mantenimiento y comportamiento responsive.

## Alcance implementado
- Se eliminó el uso de `mock repositories` en catálogo, productos admin, promociones y pedidos.
- Se removió la flag `VITE_USE_MOCK_API` del frontend para evitar caminos de ejecución inconsistentes.
- Se removió la sección de `UI Showcase` de la navegación y del router público.
- Se reemplazaron datos mock compartidos por contratos de dominio explícitos (`Product`) en módulo catálogo.
- Se preservaron reglas de negocio activas en UI:
  - bloqueo de compra sin stock,
  - guards de autenticación para checkout,
  - guards por rol en rutas administrativas,
  - invalidación de caché tras mutaciones.

## Ajustes de consistencia backend/frontend
- Todas las consultas/mutaciones de los módulos principales usan repositorios HTTP.
- El modelo de `Product` dejó de depender de `src/mocks/db.ts` y ahora vive en `catalog/domain`.

## Impacto en UX
- Navegación pública simplificada (sin ruta de showcase no productiva).
- Home conserva contenido editorial con productos destacados locales (solo visual), sin afectar contratos de API.

## Verificaciones sugeridas
1. Login/registro contra backend real.
2. Listado y detalle de catálogo.
3. Flujo carrito + checkout (usuario autenticado).
4. Panel admin: productos, stock, pedidos y promociones.
5. Revisión responsive en Home, Catálogo y tarjetas de producto.
