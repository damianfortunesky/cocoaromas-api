USE master;
GO

------------------------------------------------------------
-- 1. Cerrar conexiones activas y eliminar base si existe
------------------------------------------------------------
IF DB_ID(N'db_cocoaromas') IS NOT NULL
BEGIN
    ALTER DATABASE db_cocoaromas SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE db_cocoaromas;
END
GO

------------------------------------------------------------
-- 2. Eliminar login si existe
------------------------------------------------------------
IF EXISTS (
    SELECT 1
    FROM sys.server_principals
    WHERE name = N'sv_cocoaromas'
)
BEGIN
    DROP LOGIN sv_cocoaromas;
END
GO

------------------------------------------------------------
-- 3. Crear base de datos
------------------------------------------------------------
CREATE DATABASE db_cocoaromas;
GO

------------------------------------------------------------
-- 4. Crear login SQL Server
------------------------------------------------------------
CREATE LOGIN sv_cocoaromas
WITH PASSWORD = 'ApiUser123!';
GO

------------------------------------------------------------
-- 5. Crear usuario dentro de la base
------------------------------------------------------------
USE db_cocoaromas;
GO

CREATE USER usr_cocoaromas FOR LOGIN sv_cocoaromas;
GO

------------------------------------------------------------
-- 6. Permisos para runtime + migraciones Flyway en local
------------------------------------------------------------
ALTER ROLE db_datareader ADD MEMBER usr_cocoaromas;
GO

ALTER ROLE db_datawriter ADD MEMBER usr_cocoaromas;
GO

ALTER ROLE db_ddladmin ADD MEMBER usr_cocoaromas;
GO