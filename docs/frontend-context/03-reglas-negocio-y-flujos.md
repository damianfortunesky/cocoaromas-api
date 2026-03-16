# Frontend — Reglas de negocio y flujos

## Flujos cliente
1. Login/registro y persistencia de sesión.
2. Navegación de catálogo + detalle.
3. Carrito con validaciones de stock.
4. Checkout con confirmación de pedido.
5. Historial de pedidos del usuario.
6. Mi cuenta: perfil + direcciones.

## Flujos administrativos
1. Gestión de productos (CRUD + estado activo).
2. Gestión de categorías (modelo simplificado: nombre, slug, orden).
3. Gestión de stock con motivo de ajuste.
4. Gestión de promociones por alcance (cantidad/producto/categoría).
5. Gestión de pedidos con transición de estados.

## Reglas relevantes
- Guards de rutas según sesión y rol.
- Operaciones críticas muestran confirmaciones y errores recuperables.
- El catálogo público no debe invalidar sesión por errores de endpoints públicos.
- En checkout, no se permite confirmar sin datos mínimos del receptor.
- En pedidos admin, cambios de estado respetan transiciones válidas.
