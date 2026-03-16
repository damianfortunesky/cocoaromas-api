# 05 - Requests de la API

Este documento describe cómo invocar **todos los endpoints REST** actualmente expuestos por el backend.

## 1) Convenciones generales

- Base URL local: `http://localhost:8080`
- Prefijo API: `/api/v1`
- Content-Type para requests con body: `application/json`
- Autenticación: `Authorization: Bearer <JWT>` cuando el endpoint es privado.

Variables útiles para copiar/pegar:

```bash
BASE_URL="http://localhost:8080"
TOKEN="<jwt>"
```

---

## 2) Health

### GET /api/v1/ping

```bash
curl -X GET "$BASE_URL/api/v1/ping"
```

---

## 3) Auth

### POST /api/v1/auth/login

```bash
curl -X POST "$BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@cocoaromas.com",
    "password": "Secret123!"
  }'
```

### POST /api/v1/auth/register

```bash
curl -X POST "$BASE_URL/api/v1/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nuevo@cocoaromas.com",
    "password": "Secret123!",
    "firstName": "Ana",
    "lastName": "Pérez"
  }'
```

### GET /api/v1/auth/me

```bash
curl -X GET "$BASE_URL/api/v1/auth/me" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 4) Catálogo público

### GET /api/v1/products

Parámetros opcionales:
- `search`
- `category` (slug)
- `sort` (`name,asc` | `name,desc` | `price,asc` | `price,desc`)
- `page` (default: `0`)
- `size` (default: `12`, máximo interno: `100`)

```bash
curl -G "$BASE_URL/api/v1/products" \
  --data-urlencode "search=cacao" \
  --data-urlencode "category=sahumerios" \
  --data-urlencode "sort=price,desc" \
  --data-urlencode "page=0" \
  --data-urlencode "size=12"
```

### GET /api/v1/products/{id}

```bash
curl -X GET "$BASE_URL/api/v1/products/1"
```

### GET /api/v1/categories

```bash
curl -X GET "$BASE_URL/api/v1/categories"
```

---

## 5) Perfil de usuario autenticado

### GET /api/v1/me/profile

```bash
curl -X GET "$BASE_URL/api/v1/me/profile" \
  -H "Authorization: Bearer $TOKEN"
```

### PUT /api/v1/me/profile

```bash
curl -X PUT "$BASE_URL/api/v1/me/profile" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ana",
    "lastName": "Pérez",
    "phone": "+5491122334455",
    "dni": "30111222",
    "birthDate": "1992-04-10"
  }'
```

### GET /api/v1/me/addresses

```bash
curl -X GET "$BASE_URL/api/v1/me/addresses" \
  -H "Authorization: Bearer $TOKEN"
```

### POST /api/v1/me/addresses

```bash
curl -X POST "$BASE_URL/api/v1/me/addresses" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "label": "Casa",
    "receiverName": "Ana Pérez",
    "receiverPhone": "+5491122334455",
    "street": "Av. Siempre Viva",
    "streetNumber": "742",
    "floor": "2",
    "apartment": "A",
    "city": "CABA",
    "state": "Buenos Aires",
    "postalCode": "1000",
    "countryCode": "AR",
    "reference": "Portero eléctrico",
    "defaultShipping": true,
    "defaultBilling": true
  }'
```

### PUT /api/v1/me/addresses/{addressId}

```bash
curl -X PUT "$BASE_URL/api/v1/me/addresses/10" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "label": "Trabajo",
    "receiverName": "Ana Pérez",
    "receiverPhone": "+5491122334455",
    "street": "Corrientes",
    "streetNumber": "1234",
    "city": "CABA",
    "state": "Buenos Aires",
    "postalCode": "1043",
    "countryCode": "AR",
    "reference": "Piso 8",
    "defaultShipping": false,
    "defaultBilling": true
  }'
```

### DELETE /api/v1/me/addresses/{addressId}

```bash
curl -X DELETE "$BASE_URL/api/v1/me/addresses/10" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 6) Pedidos del cliente autenticado

### POST /api/v1/orders

```bash
curl -X POST "$BASE_URL/api/v1/orders" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "paymentMethod": "TRANSFER",
    "notes": "Entregar por la tarde",
    "deliveryMethod": "ENVIO",
    "currencyCode": "ARS",
    "items": [
      { "productId": 1, "quantity": 2 },
      { "productId": 5, "quantity": 1 }
    ]
  }'
```

### GET /api/v1/orders/me

Parámetros opcionales: `page` (default `0`), `size` (default `10`).

```bash
curl -G "$BASE_URL/api/v1/orders/me" \
  -H "Authorization: Bearer $TOKEN" \
  --data-urlencode "page=0" \
  --data-urlencode "size=10"
```

### GET /api/v1/orders/{orderId}

```bash
curl -X GET "$BASE_URL/api/v1/orders/100" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 7) Admin - Productos

> Requiere rol `ADMIN`.

### GET /api/v1/admin/products

Parámetros opcionales: `search`, `categoryId`, `active`, `page`, `size`, `sortBy`, `direction`.

```bash
curl -G "$BASE_URL/api/v1/admin/products" \
  -H "Authorization: Bearer $TOKEN" \
  --data-urlencode "search=vela" \
  --data-urlencode "categoryId=2" \
  --data-urlencode "active=true" \
  --data-urlencode "page=0" \
  --data-urlencode "size=20" \
  --data-urlencode "sortBy=updatedAt" \
  --data-urlencode "direction=desc"
```

### POST /api/v1/admin/products

```bash
curl -X POST "$BASE_URL/api/v1/admin/products" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vela Lavanda",
    "description": "Vela aromática de lavanda",
    "price": 9500,
    "categoryId": 2,
    "stockQuantity": 25,
    "imageUrl": "https://cdn.cocoaromas.com/vela-lavanda.jpg",
    "isActive": true
  }'
```

### PUT /api/v1/admin/products/{id}

```bash
curl -X PUT "$BASE_URL/api/v1/admin/products/12" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vela Lavanda Premium",
    "description": "Nueva fragancia",
    "price": 10500,
    "categoryId": 2,
    "stockQuantity": 30,
    "imageUrl": "https://cdn.cocoaromas.com/vela-lavanda-premium.jpg",
    "isActive": true
  }'
```

### DELETE /api/v1/admin/products/{id}

```bash
curl -X DELETE "$BASE_URL/api/v1/admin/products/12" \
  -H "Authorization: Bearer $TOKEN"
```

### PATCH /api/v1/admin/products/{id}/status

```bash
curl -X PATCH "$BASE_URL/api/v1/admin/products/12/status" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{ "active": false }'
```

---

## 8) Admin - Stock

> Requiere rol `ADMIN`, `OWNER` o `EMPLOYEE`.

### GET /api/v1/admin/stocks

Parámetros opcionales: `search`, `categoryId`, `available`, `lowStock`, `page`, `size`, `sortBy`, `direction`.

```bash
curl -G "$BASE_URL/api/v1/admin/stocks" \
  -H "Authorization: Bearer $TOKEN" \
  --data-urlencode "available=true" \
  --data-urlencode "lowStock=true" \
  --data-urlencode "page=0" \
  --data-urlencode "size=20"
```

### GET /api/v1/admin/stocks/{productId}

```bash
curl -X GET "$BASE_URL/api/v1/admin/stocks/12" \
  -H "Authorization: Bearer $TOKEN"
```

### PATCH /api/v1/admin/stocks/{productId}

```bash
curl -X PATCH "$BASE_URL/api/v1/admin/stocks/12" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "newStockQuantity": 50,
    "adjustment": 20,
    "reason": "Ingreso de mercadería"
  }'
```

---

## 9) Admin - Pedidos

> Requiere rol `ADMIN` o `EMPLOYEE`.

### GET /api/v1/admin/orders

Parámetros opcionales:
- `search`
- `status`
- `paymentMethod`
- `createdFrom` (ISO-8601)
- `createdTo` (ISO-8601)
- `page`, `size`, `sortBy`, `direction`

```bash
curl -G "$BASE_URL/api/v1/admin/orders" \
  -H "Authorization: Bearer $TOKEN" \
  --data-urlencode "status=PENDIENTE" \
  --data-urlencode "paymentMethod=TRANSFER" \
  --data-urlencode "createdFrom=2026-01-01T00:00:00Z" \
  --data-urlencode "createdTo=2026-12-31T23:59:59Z" \
  --data-urlencode "page=0" \
  --data-urlencode "size=20"
```

### GET /api/v1/admin/orders/{orderId}

```bash
curl -X GET "$BASE_URL/api/v1/admin/orders/100" \
  -H "Authorization: Bearer $TOKEN"
```

### PATCH /api/v1/admin/orders/{orderId}/status

```bash
curl -X PATCH "$BASE_URL/api/v1/admin/orders/100/status" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "newStatus": "PAGADO",
    "reason": "Pago acreditado"
  }'
```

---

## 10) Admin - Promociones

> Requiere rol `ADMIN`.

### GET /api/v1/admin/promotions

Parámetros opcionales: `search`, `promotionType`, `active`, `currentlyValid`, `page`, `size`, `sortBy`, `direction`.

```bash
curl -G "$BASE_URL/api/v1/admin/promotions" \
  -H "Authorization: Bearer $TOKEN" \
  --data-urlencode "promotionType=PRODUCT" \
  --data-urlencode "active=true" \
  --data-urlencode "currentlyValid=true" \
  --data-urlencode "page=0" \
  --data-urlencode "size=20"
```

### POST /api/v1/admin/promotions

```bash
curl -X POST "$BASE_URL/api/v1/admin/promotions" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Promo otoño",
    "description": "15% en productos seleccionados",
    "promotionType": "PRODUCT",
    "discountType": "PERCENTAGE",
    "discountValue": 15,
    "minimumQuantity": 1,
    "productId": 12,
    "categoryId": null,
    "active": true,
    "startsAt": "2026-04-01T00:00:00Z",
    "endsAt": "2026-04-30T23:59:59Z"
  }'
```

### PUT /api/v1/admin/promotions/{id}

```bash
curl -X PUT "$BASE_URL/api/v1/admin/promotions/7" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Promo otoño extendida",
    "description": "20% en productos seleccionados",
    "promotionType": "PRODUCT",
    "discountType": "PERCENTAGE",
    "discountValue": 20,
    "minimumQuantity": 1,
    "productId": 12,
    "categoryId": null,
    "active": true,
    "startsAt": "2026-04-01T00:00:00Z",
    "endsAt": "2026-05-15T23:59:59Z"
  }'
```

### PATCH /api/v1/admin/promotions/{id}/status

```bash
curl -X PATCH "$BASE_URL/api/v1/admin/promotions/7/status" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{ "active": false }'
```

### DELETE /api/v1/admin/promotions/{id}

```bash
curl -X DELETE "$BASE_URL/api/v1/admin/promotions/7" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 11) Admin - Categorías

> Requiere rol `ADMIN`, `OWNER` o `EMPLOYEE`.

### GET /api/v1/admin/categories

```bash
curl -X GET "$BASE_URL/api/v1/admin/categories" \
  -H "Authorization: Bearer $TOKEN"
```

### POST /api/v1/admin/categories

```bash
curl -X POST "$BASE_URL/api/v1/admin/categories" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Difusores",
    "slug": "difusores",
    "displayOrder": 3
  }'
```

### PUT /api/v1/admin/categories/{id}

```bash
curl -X PUT "$BASE_URL/api/v1/admin/categories/3" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Difusores premium",
    "slug": "difusores-premium",
    "displayOrder": 3
  }'
```

### DELETE /api/v1/admin/categories/{id}

```bash
curl -X DELETE "$BASE_URL/api/v1/admin/categories/3" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 12) Recomendación

Para validar contratos de request/response en tiempo real:

- Swagger UI: `GET /swagger-ui/index.html`
- OpenAPI JSON: `GET /v3/api-docs`
