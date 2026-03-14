# Task 11 - Registro público de usuarios

## Objetivo
Implementar flujo de registro para nuevos clientes sin intervención administrativa.

## Alcance implementado
- Formulario de registro público.
- Validaciones de campos obligatorios y formato.
- Consumo de endpoint de alta de usuario cliente.
- Redirección al login o sesión inicial tras registro exitoso.

## Contexto para backend
- Frontend no envía rol; el backend define `CLIENT` por defecto.
- Se muestran mensajes claros ante duplicados o validaciones fallidas.

## Criterios de aceptación
- Usuario nuevo puede registrarse con datos válidos.
- Duplicados y errores se informan sin exponer detalles sensibles.
- Flujo completo de onboarding queda operativo.

## Trazabilidad
- Tipo: adquisición de usuarios.
- Dependencias: Task 02.
- Desbloquea: crecimiento orgánico de base de clientes.
