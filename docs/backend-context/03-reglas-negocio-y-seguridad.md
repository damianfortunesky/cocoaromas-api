# Reglas de negocio y seguridad

## Reglas de stock y checkout

- No se descuenta stock al crear pedido (`POST /orders`).
- Los productos sin stock se mantienen visibles en catálogo.
- El decremento de stock queda diferido al evento de pago/confirmación.
- En gestión administrativa de stock no se permite terminar con cantidad negativa.

## Reglas de pedidos

- Política explícita de transiciones de estado para evitar saltos inválidos.
- Cambios de estado inválidos deben rechazarse con error controlado.
- El cambio a `PAGADO` tiene un punto de extensión para procesos posteriores.

## Reglas de promociones

- Validaciones por tipo de promoción y tipo de descuento.
- Validación de vigencia con fechas coherentes.
- Borrado lógico para preservar trazabilidad histórica.

## Seguridad y permisos por rol

### Matriz de permisos consolidada

- `admin`: acceso total, incluyendo gestión de roles.
- `owner/admin/employee`: actualización de stock.
- `admin/employee`: cambio de estado de pedidos.
- Registro público siempre crea rol `CLIENT`.

### Principios aplicados

- Endpoints administrativos protegidos por rol.
- Respuestas de autenticación/autorización estandarizadas.
- Frontend puede construir guards por rol con una matriz consistente.

## Integración frontend-backend

- CORS habilitado para desarrollo local: `http://localhost:5173`.
- Métodos HTTP y preflight `OPTIONS` permitidos.
- Headers de autenticación (`Authorization`) y JSON habilitados.
- Configuración alineada con Spring Security para evitar bloqueos de preflight.

## Refactor técnico relevante

En creación de pedidos se optimizó la carga de productos con cache por request (`productId`) para evitar consultas repetidas cuando un mismo producto aparece en múltiples ítems/variantes.

### Resultado esperado

- Menor cantidad de accesos redundantes a persistencia durante checkout.
- Sin cambios en contrato API ni reglas funcionales existentes.
