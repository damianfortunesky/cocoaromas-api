# Task 13 - Configuración CORS para frontend local

## Objetivo
Permitir que el frontend en desarrollo (`http://localhost:5173`) consuma la API del backend (`http://localhost:8080`) sin bloqueos del navegador por política CORS.

## Alcance implementado
- Se agregó configuración CORS global en `infrastructure/config/CorsConfig.java`.
- Se habilitó origen permitido para desarrollo local: `http://localhost:5173`.
- Se permitieron métodos HTTP usados por el frontend, incluyendo `OPTIONS` para preflight.
- Se permitieron headers requeridos por autenticación (`Authorization`) y payload JSON.
- Se mantuvo CORS habilitado en Spring Security y se autorizó explícitamente `OPTIONS /**` para que los preflight no sean bloqueados por autenticación.

## Contexto para frontend
- El login y el resto de llamadas desde Vite (`localhost:5173`) dejan de fallar por CORS.
- Los preflight (`OPTIONS`) ahora reciben respuesta correcta y permiten continuar con la request real.

## Criterios de aceptación
- El backend devuelve headers CORS para origen local permitido.
- Requests preflight (`OPTIONS`) permitidas.
- Flujo de login desde frontend funcional sin errores CORS.
- Swagger y endpoints públicos se mantienen accesibles.

## Trazabilidad
- Tipo: integración frontend-backend en entorno de desarrollo.
- Dependencias: Task 02 (Auth JWT), Task 07 (Security), Task 12 (Refactor).
