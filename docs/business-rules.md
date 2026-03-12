# Reglas de negocio

## Roles
- admin
- owner
- employee
- client

## Permisos
- admin: acceso total
- admin: único rol que puede gestionar roles
- owner/admin/employee: pueden actualizar stock
- admin/employee: pueden cambiar estado de pedidos

## Pedidos
Estados:
- pendiente
- esperando_pago
- pagado
- preparando
- enviado
- entregado
- cancelado

## Stock
- el stock no se descuenta al crear el pedido
- el stock se descuenta al pagar
- debe soportarse stock simple o por variante

## Productos
- productos sin stock deben seguir visibles
- deben mostrarse como no disponibles

## Promociones
- administrables desde panel
- por cantidad
- por producto
- por categoría
- porcentaje o monto fijo
- vigencia opcional