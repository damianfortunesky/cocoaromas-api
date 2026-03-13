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
│   ├── port/out
│   └── service
├── infrastructure            # Configuraciones y adapters técnicos
│   ├── config
│   ├── persistence
│   └── security
└── entrypoints/rest          # Controladores HTTP
```

## Ejecutar local

```bash
cp .env.example .env
set -a; source .env; set +a
mvn spring-boot:run
```

Por defecto usa perfil `local`.

## Profiles

- `local`: pensado para conectar a SQL Server instalado localmente.
- `docker`: pensado para correr dentro de contenedor y apuntar al SQL Server del host.

## Variables de base de datos

- `DB_HOST` (default: `localhost` en local, `host.docker.internal` en docker)
- `DB_PORT` (default: `1433`)
- `DB_NAME` (default recomendado: `db_cocoaromas`)
- `DB_USERNAME` (default recomendado: `sv_cocoaromas`)
- `DB_PASSWORD` (default recomendado: `ApiUser123!`)
- `DB_ENCRYPT` (default: `false`)

## Variables JWT

- `JWT_SECRET`: secreto usado para firmar/verificar JWT (mínimo 32 caracteres)
- `JWT_EXPIRATION_SECONDS`: expiración del access token en segundos (default: `3600`)
- `JWT_ISSUER`: issuer del token (default: `cocoaromas-api`)

## Flujo de autenticación implementado

1. `POST /api/v1/auth/login` recibe `emailOrUsername` + `password`.
2. El caso de uso busca usuario por email o username en SQL Server.
3. Se valida password con `PasswordEncoder`.
4. Si es válido, se emite JWT (sin refresh token por ahora) con `sub=userId` y `role`.
5. El frontend usa `Authorization: Bearer <token>`.
6. `GET /api/v1/auth/me` devuelve el usuario autenticado actual.

## Usuarios de prueba seed

- `admin` / `Admin123!` (role: `admin`)
- `owner` / `Owner123!` (role: `owner`)
- `employee` / `Employee123!` (role: `employee`)
- `client` / `Client123!` (role: `client`)

También podés loguear con los emails `*@cocoaromas.local`.

## Endpoints principales

- Ping: `GET /api/v1/ping`
- Auth Login: `POST /api/v1/auth/login`
- Auth Me: `GET /api/v1/auth/me`
- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`
- Actuator Health: `/actuator/health`
- Prometheus: `/actuator/prometheus`

## Probar auth con curl

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"emailOrUsername":"admin","password":"Admin123!"}'
```

Luego usar `accessToken`:

```bash
curl http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer <TOKEN>"
```

## Swagger

- Abrí `/swagger-ui.html`.
- Usá `POST /api/v1/auth/login` con el payload indicado.
- Botón **Authorize**: pegar `Bearer <TOKEN>` para probar `/api/v1/auth/me`.

## Docker

Levanta únicamente la API backend:

```bash
docker compose up --build
```

> El `docker-compose.yml` ya incluye variables DB/JWT para `db_cocoaromas`.

> El SQL Server no se levanta en este compose porque se espera usar una instancia local existente.
