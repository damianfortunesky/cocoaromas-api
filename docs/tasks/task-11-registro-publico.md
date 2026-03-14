# Task 11 - Registro público de usuarios

## Objetivo
Incorporar onboarding para clientes finales sin intervención administrativa.

## Alcance implementado
- `POST /api/v1/auth/register` público.
- Alta de usuario con rol forzado `CLIENT`.
- Validación de unicidad de email/username.
- Persistencia de password hasheada y datos básicos.

## Contexto para frontend
- Permite flujo completo de registro + login.
- Evita que frontend envíe o gestione roles en alta pública.

## Criterios de aceptación
- Registro exitoso con datos válidos.
- Rechazo controlado ante duplicados o formato inválido.
- Endpoint accesible sin token.

## Trazabilidad
- Tipo: adquisición de usuarios.
- Dependencias: Task 02.
