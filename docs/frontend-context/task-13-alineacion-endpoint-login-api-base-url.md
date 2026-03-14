# Task 13 - Alineación endpoint de login y base URL de API

## Objetivo
Alinear el frontend con el contrato real del backend para autenticación y consolidar la configuración de la URL base de API en una única fuente.

## Cambios implementados
- Se configuró `VITE_API_URL` con el valor por defecto `http://localhost:8080/api/v1`.
- Se agregaron `.env` y `.env.example` para estandarizar la configuración local del frontend.
- Se reforzó la normalización de la URL base en `src/shared/config/env.ts` para evitar errores por barras finales.
- Se mantuvo el endpoint relativo de login (`/auth/login`) para que el cliente HTTP resuelva correctamente a `http://localhost:8080/api/v1/auth/login`.

## Resultado esperado
- El flujo de login deja de fallar por endpoint incorrecto (`/api/auth/login`).
- El frontend consume el endpoint correcto del backend (`/api/v1/auth/login`).
- La configuración de API queda centralizada y documentada para evitar inconsistencias futuras.

## Trazabilidad
- Tipo: integración frontend-backend.
- Dependencias: Task 02 (autenticación JWT).
- Desbloquea: inicio de sesión funcional en entornos locales con backend en `localhost:8080`.
