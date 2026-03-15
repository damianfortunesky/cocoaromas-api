# TASK 22 — Simplificar modelo de Categorías en Frontend

## Objetivo
Alinear el módulo **Admin → Categorías** con el nuevo contrato de backend, dejando a la categoría como entidad mínima para clasificación.

## Cambios aplicados

### 1) Modelo de dominio actualizado
Se actualizó el tipo `Category` y el payload de alta/edición (`CategoryUpsertInput`) para usar únicamente:

- `id: string`
- `name: string`
- `slug: string`
- `displayOrder: number`

Se removieron campos legacy que ya no forman parte del modelo: `description`, `active`, y otros metadatos administrativos.

### 2) Adaptación del repositorio API de categorías
Se ajustó el mapping de DTO en frontend para contemplar el nuevo campo `displayOrder` y devolver un objeto de dominio consistente con el nuevo modelo.

### 3) Refactor del formulario Admin → Categorías
El formulario de creación/edición ahora expone solo:

- `Nombre`
- `Orden de visualización`

Se eliminaron los inputs de `Slug` y `Descripción`.

### 4) Generación automática de slug
El `slug` se genera automáticamente desde `name` antes de enviar el payload al backend con reglas:

- lowercase
- remoción de acentos
- espacios a guiones
- limpieza de caracteres no válidos

### 5) Payload de create/update
Tanto create como update envían:

```json
{
  "name": "...",
  "slug": "...",
  "displayOrder": 0
}
```

### 6) Listado de categorías
La tabla de administración ahora muestra columnas:

- Orden
- Nombre
- Slug
- Acciones

Y se ordena por defecto por `displayOrder ASC` en frontend.

## Resultado
El módulo de categorías queda simplificado, sin campos administrativos innecesarios, y alineado al backend actual.
