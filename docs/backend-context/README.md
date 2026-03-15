# Backend context unificado

Este directorio consolidó los archivos históricos `task-xx-*.md` en documentos temáticos para reducir ruido y facilitar el onboarding técnico.

## Estructura actual

1. [01-arquitectura-y-plataforma.md](./01-arquitectura-y-plataforma.md)
2. [02-contratos-api.md](./02-contratos-api.md)
3. [03-reglas-negocio-y-seguridad.md](./03-reglas-negocio-y-seguridad.md)
4. [04-modelo-datos-y-evolucion.md](./04-modelo-datos-y-evolucion.md)

## Cobertura de información

La consolidación incluye y reclasifica el contenido relevante de las tasks 01 a 17:

- Fundaciones del backend, perfiles, observabilidad y despliegue.
- Contratos públicos y administrativos (auth, catálogo, pedidos, stock, promociones, categorías, perfil y direcciones).
- Reglas de negocio transversales (stock, checkout, permisos por rol, transiciones de pedidos, CORS).
- Evolución de modelo de datos y ajustes ORM/DB para compatibilidad.

## Criterios de esta unificación

- Menos archivos y más enfoque por dominio funcional.
- Eliminación de duplicados (por ejemplo, tasks repetidas de simplificación de productos).
- Lenguaje orientado a implementación y consumo (frontend + backend).
- Conservación de trazabilidad por bloques temáticos en vez de prompts aislados.
