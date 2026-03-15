# Arquitectura y plataforma backend

## Stack y base técnica

- Java 17 + Spring Boot (Maven).
- Arquitectura Ports & Adapters con capas:
  - `domain`
  - `application`
  - `infrastructure`
  - `entrypoints/rest`
- Base URL de API: `/api/v1`.

## Entorno y configuración

- Perfiles de ejecución: `local`, `docker`.
- Variables de entorno para conexión de DB y configuración JWT.
- Contenedorización disponible con `Dockerfile` y `docker-compose.yml`.

## Endpoints técnicos y observabilidad

- Healthcheck técnico (`/api/v1/ping`).
- OpenAPI/Swagger para validación de contratos durante integración.
- Actuator y métricas Prometheus habilitadas.

## Objetivos de arquitectura alcanzados

- Soporte para crecimiento modular (catálogo, auth, pedidos, administración).
- Estandarización de configuración para desarrollo local y escenarios Docker.
- Base de trazabilidad operativa con monitoreo y documentación API.
