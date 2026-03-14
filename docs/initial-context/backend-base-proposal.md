# Propuesta base de backend (alineada a frontend existente)

Este documento propone la estructura inicial del backend y el orden de implementación **sin modificar reglas de negocio ni contrato** definido en `docs/`.

## 1) Stack y lineamientos técnicos

- Java 17
- Spring Boot (Web, Validation, Security, Data JPA)
- Maven
- SQL Server
- Docker / Docker Compose
- Swagger (springdoc-openapi)
- Spring Boot Actuator
- Micrometer + Prometheus
- Arquitectura Ports and Adapters (Hexagonal)

## 2) Estructura de proyecto propuesta (Maven)

```text
cocoaromas-api/
├─ pom.xml
├─ Dockerfile
├─ docker-compose.yml
├─ README.md
├─ .env.example
├─ src/
│  ├─ main/
│  │  ├─ java/com/cocoaromas/api/
│  │  │  ├─ CocoaromasApiApplication.java
│  │  │  ├─ shared/
│  │  │  │  ├─ domain/
│  │  │  │  │  ├─ exception/
│  │  │  │  │  └─ valueobject/
│  │  │  │  ├─ application/
│  │  │  │  │  └─ security/
│  │  │  │  └─ infrastructure/
│  │  │  │     ├─ config/
│  │  │  │     ├─ exception/
│  │  │  │     └─ mapper/
│  │  │  ├─ auth/
│  │  │  │  ├─ domain/
│  │  │  │  ├─ application/
│  │  │  │  │  ├─ port/in/
│  │  │  │  │  ├─ port/out/
│  │  │  │  │  └─ service/
│  │  │  │  └─ infrastructure/
│  │  │  │     ├─ in/web/
│  │  │  │     └─ out/persistence/
│  │  │  ├─ catalog/
│  │  │  │  ├─ domain/
│  │  │  │  ├─ application/
│  │  │  │  └─ infrastructure/
│  │  │  ├─ order/
│  │  │  │  ├─ domain/
│  │  │  │  ├─ application/
│  │  │  │  └─ infrastructure/
│  │  │  ├─ stock/
│  │  │  │  ├─ domain/
│  │  │  │  ├─ application/
│  │  │  │  └─ infrastructure/
│  │  │  ├─ promotion/
│  │  │  │  ├─ domain/
│  │  │  │  ├─ application/
│  │  │  │  └─ infrastructure/
│  │  │  └─ admin/
│  │  │     └─ infrastructure/in/web/
│  │  └─ resources/
│  │     ├─ application.yml
│  │     ├─ application-dev.yml
│  │     ├─ application-docker.yml
│  │     └─ db/migration/ (Flyway)
│  └─ test/
│     └─ java/com/cocoaromas/api/
│        ├─ unit/
│        ├─ integration/
│        └─ contract/
```

## 3) Modelo de dominios inicial (alineado a reglas)

### 3.1 Auth y usuarios
- `User`
- `Role` (admin, owner, employee, client)
- Casos de uso:
  - login (`POST /api/v1/auth/login`)
  - usuario actual (`GET /api/v1/auth/me`)

### 3.2 Catálogo
- `Product` (activo/inactivo, visible)
- `Category`
- `ProductAttribute` (clave-valor flexible)
- `ProductVariant` (opcional)
- `VariantAttribute`

> Soporta modelo flexible de producto y variantes opcionales.

### 3.3 Stock
- `StockItem` por producto y/o variante
- Reglas:
  - sin descuento al crear pedido
  - descuento solo al confirmar pago
  - producto sin stock sigue visible (no disponible)

### 3.4 Pedidos
- `Order`
- `OrderItem`
- `OrderStatus`:
  - pendiente, esperando_pago, pagado, preparando, enviado, entregado, cancelado
- Reglas:
  - cliente autenticado crea pedido
  - cambio de estado admin/employee

### 3.5 Promociones
- `Promotion`
- `PromotionType` (porcentaje / monto fijo)
- `PromotionScope` (cantidad / producto / categoría)
- Vigencia opcional (desde/hasta)
- Activación/desactivación desde panel admin

## 4) API: estrategia de alineación con frontend

Se mantiene el draft como base de endpoints (`docs/api-contract-draft.md`) y se completan:

- DTOs request/response por endpoint
- códigos HTTP
- errores estándar (ej. validación, no autorizado, sin permisos, no encontrado)
- paginación/filtros para listados (`products`, `admin/orders`, `admin/stocks`, `admin/promotions`)

Sin romper rutas existentes del frontend.

## 5) Seguridad y autorización

- JWT stateless
- `admin`: acceso total
- gestión de roles: solo `admin`
- stock: `owner`, `admin`, `employee`
- estado de pedido: `admin`, `employee`

Implementación sugerida:
- filtros JWT + `SecurityFilterChain`
- autorización en capa de aplicación (además de anotaciones en controller)

## 6) Observabilidad

- Actuator habilitado (`/actuator/health`, `/actuator/info`, `/actuator/prometheus`)
- Métricas de negocio mínimas:
  - cantidad de pedidos creados
  - pedidos pagados
  - cambios de estado de pedido
  - actualizaciones de stock
- logs estructurados por request (con traceId)

## 7) Orden de implementación recomendado

### Fase 0 — bootstrap técnico
1. Crear esqueleto Spring Boot + Maven + Java 17
2. Configurar SQL Server + Flyway
3. Configurar Swagger, Actuator y Prometheus
4. Configurar Dockerfile y docker-compose

### Fase 1 — seguridad y auth
1. Modelo `User/Role`
2. JWT login (`POST /auth/login`)
3. `GET /auth/me`
4. Base de autorización por roles

### Fase 2 — catálogo público
1. Entidades `Category`, `Product`, atributos y variantes
2. Endpoints públicos:
   - `GET /products`
   - `GET /products/{id}`
   - `GET /categories`
3. Regla de visibilidad sin stock (visible pero no disponible)

### Fase 3 — carrito/checkout backend mínimo (pedidos cliente)
1. `POST /orders`
2. `GET /orders/me`
3. `GET /orders/{id}` con validación de pertenencia
4. Estado inicial `pendiente` / `esperando_pago` según flujo

### Fase 4 — pagos y descuento real de stock
1. Confirmación de pago (flujo transferencia)
2. Transición a `pagado`
3. Recién aquí descontar stock
4. Dejar puerto de pago listo para adapter futuro Mercado Pago

### Fase 5 — admin productos y stock
1. `POST/PUT/DELETE/PATCH status` de productos
2. `GET /admin/stocks`
3. `PATCH /admin/stocks/{productId}`
4. Permisos por rol según reglas

### Fase 6 — admin pedidos
1. `GET /admin/orders`
2. `PATCH /admin/orders/{orderId}/status`
3. Restringir cambio de estado a admin/employee

### Fase 7 — promociones
1. Modelo y motor de aplicación de promociones
2. Endpoints admin promociones
3. Integrar cálculo en checkout/pedido

### Fase 8 — hardening
1. Tests unitarios de casos de uso
2. Tests de integración de adapters y DB
3. Tests de contrato sobre endpoints del draft
4. Métricas y alertas básicas

## 8) Definiciones para evitar desalineación con frontend

Antes de codificar cada módulo:
- confirmar payloads exactos con frontend (campos obligatorios/opcionales)
- congelar DTOs en OpenAPI
- agregar tests de contrato para rutas de `docs/api-contract-draft.md`

Con esto, la API evoluciona sin romper consumo del frontend ya construido.
