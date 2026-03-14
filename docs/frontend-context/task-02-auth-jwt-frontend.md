# Task 02 - Autenticación JWT en frontend

## Objetivo
Implementar login/logout con token JWT y persistencia de sesión para usuarios cliente/admin.

## Alcance implementado
- Pantalla/formulario de login.
- Consumo de endpoint de autenticación y almacenamiento seguro del token.
- Inyección del token en requests autenticados.
- Logout con limpieza de sesión y redirección.

## Contexto para backend
- Contrato esperado de login con `accessToken` y datos mínimos de usuario.
- Frontend reacciona a `401/403` cerrando sesión cuando corresponde.

## Criterios de aceptación
- Usuario válido puede iniciar sesión.
- Usuario inválido visualiza error controlado.
- Rutas protegidas requieren token activo.

## Trazabilidad
- Tipo: identidad y acceso.
- Dependencias: Task 01.
- Desbloquea: áreas privadas de cliente y administración.
