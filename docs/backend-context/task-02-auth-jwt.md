# Task 02 - Modelo inicial de usuarios y autenticación JWT

## Objetivo
Implementar login con JWT y endpoint de usuario autenticado para habilitar flujos de sesión en frontend.

## Alcance implementado
- Modelo de usuario con roles (`admin`, `owner`, `employee`, `client`).
- `POST /api/v1/auth/login`.
- `GET /api/v1/auth/me`.
- Integración de Spring Security + filtro JWT + hash de passwords.
- Seeds de usuarios de prueba para QA/integración.

## Contexto para frontend
- Guardar `accessToken` y enviarlo como `Bearer token`.
- Resolver navegación por rol desde `/auth/me`.
- No hay refresh token en esta etapa.

## Criterios de aceptación
- Login válido retorna token y datos mínimos de usuario.
- Login inválido retorna error controlado.
- `/auth/me` responde correctamente con y sin token.

## Trazabilidad
- Tipo: autenticación y autorización inicial.
- Dependencias: Task 01.
