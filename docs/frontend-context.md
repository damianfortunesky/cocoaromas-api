# Contexto del frontend

Ya existe un frontend ecommerce desarrollado en React + TypeScript + SCSS Modules.

## Módulos ya implementados en frontend
- auth
- catálogo
- detalle de producto
- carrito
- checkout
- mis pedidos
- panel admin
- administración de productos
- gestión de stock
- gestión de pedidos
- gestión de promociones
- UI Showcase / design system

## Reglas ya definidas
- solo usuarios logueados pueden comprar
- roles: admin, owner, employee, client
- admin puede gestionar todo
- admin es el único que puede gestionar roles
- owner, admin y employee pueden modificar stock
- admin y employee pueden cambiar el estado de los pedidos
- productos sin stock siguen visibles pero sombreados
- el stock se descuenta únicamente al pagar
- promociones se administran desde panel
- se debe contemplar transferencia bancaria y futura integración con Mercado Pago

## Modelo de producto esperado
Los productos no tienen un modelo rígido de variantes.
Se necesita soportar:
- categorías
- atributos flexibles por producto
- variantes opcionales cuando aplique
- stock simple o stock por variante

Ejemplos:
- sahumerios: marca, aroma, presentación
- perfumes: marca, fragancia, contenido
- remeras: talle, color
- jabones: aroma, peso
- shampoo para autos: marca, contenido, tipo
- adornos: material, estilo, tamaño