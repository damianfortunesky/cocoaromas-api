# Task 03 - Catálogo público en storefront

## Objetivo
Exponer el catálogo público para navegación sin autenticación, con listado y detalle de productos.

## Alcance implementado
- Vista de listado de productos con filtros básicos.
- Vista de detalle de producto.
- Integración con categorías y datos públicos.
- Manejo de estados de carga, vacío y error.

## Contexto para backend
- Requiere endpoints públicos consistentes para catálogo y detalle.
- Frontend depende de stock visible y precio final para decisión de compra.

## Criterios de aceptación
- Visitante puede navegar catálogo sin login.
- Detalle muestra información completa del producto.
- Errores de API se muestran de forma amigable.

## Trazabilidad
- Tipo: adquisición y descubrimiento.
- Dependencias: Task 01.
- Desbloquea: carrito y flujo de checkout.
