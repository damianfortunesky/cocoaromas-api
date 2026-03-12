# cocoaromas-api

Bootstrap inicial del backend ecommerce con arquitectura **Ports and Adapters (Hexagonal)**.

## Stack
- Java 17
- Spring Boot 3
- Maven
- SQL Server
- Swagger / OpenAPI
- Actuator + Prometheus
- Docker

## Estructura base

```text
src/main/java/com/cocoaromas/api
├── domain                    # Núcleo del dominio (sin dependencias de Spring)
├── application               # Casos de uso y puertos de entrada/salida
│   ├── port/in
│   └── service
├── infrastructure            # Configuraciones y adapters técnicos
│   └── config
└── entrypoints/rest          # Controladores HTTP
```

## Ejecutar local

```bash
mvn spring-boot:run
```

Por defecto usa perfil `local`.

## Profiles

- `local`: pensado para conectar a SQL Server instalado localmente.
- `docker`: pensado para correr dentro de contenedor y apuntar al SQL Server del host.

## Variables de base de datos

- `DB_HOST` (default: `localhost` en local, `host.docker.internal` en docker)
- `DB_PORT` (default: `1433`)
- `DB_NAME` (default: `cocoaromas`)
- `DB_USERNAME` (default: `sa`)
- `DB_PASSWORD` (default: `YourStrong!Passw0rd`)
- `DB_ENCRYPT` (default: `false`)

## Endpoints de soporte

- Ping: `GET /api/v1/ping`
- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`
- Actuator Health: `/actuator/health`
- Prometheus: `/actuator/prometheus`

## Docker

Levanta únicamente la API backend:

```bash
docker compose up --build
```

> El SQL Server no se levanta en este compose porque se espera usar una instancia local existente.
