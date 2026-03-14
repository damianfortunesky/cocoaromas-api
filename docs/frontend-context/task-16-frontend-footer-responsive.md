# Task Frontend — Footer responsive

## Objetivo
Crear un footer responsive para la vista pública de CocoAromas, con espacio para información de contacto y dirección del local, manteniendo una estética simple y moderna.

## Cambios implementados

### 1) Nuevo componente `Footer`
Se creó un componente reutilizable para la parte inferior del layout público con:
- Bloque de marca (`CocoAromas`) y breve descripción.
- Bloque de **Contacto** con teléfono y mail clickeables.
- Bloque de **Dirección** con ubicación y horario.
- Línea legal con año dinámico (`© {año}`) generado en runtime.

Archivos:
- `src/shared/ui/Footer/Footer.tsx`
- `src/shared/ui/Footer/Footer.module.scss`

### 2) Integración en layout público
Se integró el footer en `PublicLayout` para que aparezca en todas las rutas públicas.
Además, se agregó una estructura flex vertical para mantener comportamiento consistente en distintas alturas de contenido.

Archivos:
- `src/layouts/PublicLayout.tsx`
- `src/layouts/PublicLayout.module.scss`

## Diseño responsive aplicado
- Layout principal del footer con `flex` para escritorio.
- Conversión a columna en pantallas medianas.
- Grilla de información que pasa de 2 columnas a 1 columna en mobile.
- Ajuste de paddings, radios y tipografías para pantallas pequeñas.

## Resultado esperado
- Footer visible en todas las páginas públicas.
- Sección clara para contacto y dirección.
- Estética moderna, limpia y consistente con el diseño actual del proyecto.
- Correcta adaptación a desktop, tablet y mobile.
