# Task 04 - Flujo de carrito y pedidos del cliente

## Objetivo
Permitir al cliente construir su carrito, confirmar checkout y consultar sus pedidos.

## Alcance implementado
- Gestión de carrito (agregar/quitar/actualizar cantidades).
- Pantalla de checkout y confirmación de orden.
- Listado/historial de pedidos del usuario autenticado.
- Validación de sesión y feedback de operación.

## Contexto para backend
- Consumo de endpoints de creación y consulta de pedidos.
- El frontend respeta validaciones de stock/precio devueltas por API.

## Criterios de aceptación
- Cliente autenticado puede finalizar compra válida.
- Pedido creado se refleja en historial.
- Flujos inválidos retornan mensajes claros.

## Trazabilidad
- Tipo: core de conversión.
- Dependencias: Task 02, Task 03.
- Desbloquea: operación end-to-end de compra.
