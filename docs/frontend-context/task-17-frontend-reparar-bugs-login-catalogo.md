# Task Frontend - Reparar bugs login, catálogo y home

## Objetivo
Corregir incidencias detectadas en el flujo de autenticación/navegación y en la Home:

- Evitar cierre de sesión incorrecto al navegar al catálogo luego de iniciar sesión.
- Agregar opción de "recordar mi contraseña" en la pantalla de login.
- Dejar vacía la sección de productos destacados en Home hasta que se carguen desde la app.

## Cambios implementados

### 1) Corrección de sesión al navegar a catálogo

Se ajustó el manejo global de errores `401` del cliente HTTP para que **no fuerce logout** en endpoints públicos.

Antes: cualquier `401` disparaba cierre de sesión global.

Ahora: solo se dispara el handler de sesión expirada cuando el `401` proviene de endpoints protegidos. Se excluyeron rutas públicas:

- `/catalog/`
- `/auth/login`
- `/auth/register`

Con esto, si catálogo (público) responde `401` por un problema externo, no invalida una sesión ya iniciada.

### 2) Login con opción "Recordar mi contraseña"

En el formulario de login se agregó:

- Campo checkbox: `Recordar mi contraseña`.
- Valor por defecto activo (`true`) para una UX más predecible.
- Estilos visuales para el checkbox en el módulo SCSS de login.

Además, se preservó el contrato de autenticación enviando al backend solo:

- `email`
- `password`

### 3) Home sin destacados mockeados

Se removió el contenido aleatorio/local hardcodeado de "Productos destacados" en la Home.

Ahora:

- `featuredProducts` inicia como colección vacía.
- Si está vacío, se muestra el mensaje: `Aún no hay productos destacados cargados.`
- El texto de sección aclara que se completará cuando se carguen destacados desde el panel/app.

## Impacto funcional

- Mejora de estabilidad de sesión para usuarios autenticados.
- Login más completo con opción de recordar contraseña.
- Home consistente con estado real del negocio (sin datos ficticios en destacados).

## Archivos modificados

- `src/shared/api/httpClient.ts`
- `src/modules/auth/presentation/pages/LoginPage.tsx`
- `src/modules/auth/presentation/pages/LoginPage.module.scss`
- `src/pages/HomePage.tsx`
