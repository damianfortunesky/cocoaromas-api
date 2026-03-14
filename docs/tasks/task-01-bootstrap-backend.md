# Task 01 - Bootstrap backend y arquitectura base

## Objetivo
Crear la base de la API con Java 17 + Spring Boot y arquitectura Ports & Adapters para soportar evolución por módulos.

## Alcance implementado
- Proyecto Maven con estructura por capas: `domain`, `application`, `infrastructure`, `entrypoints/rest`.
- Configuración de perfiles (`local`, `docker`) y variables de entorno para DB/JWT.
- Exposición de endpoints técnicos (`/api/v1/ping`, OpenAPI, Actuator, Prometheus).
- Base de contenedorización (`Dockerfile` y `docker-compose.yml`).

## Contexto para frontend
- URL base esperada: `/api/v1`.
- Disponibilidad de Swagger para validar rápidamente payloads durante integración.

## Criterios de aceptación
- La app inicia localmente con `mvn spring-boot:run`.
- Endpoint de salud funcional.
- Documentación API visible en Swagger.

## Trazabilidad
- Tipo: infraestructura fundacional.
- Dependencias de esta task: ninguna.
- Desbloquea: autenticación, catálogo, pedidos, módulos admin.
