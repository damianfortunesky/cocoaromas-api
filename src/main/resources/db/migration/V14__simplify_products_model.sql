IF COL_LENGTH('dbo.products', 'description') IS NULL
BEGIN
    ALTER TABLE dbo.products ADD description NVARCHAR(2000) NULL;
END
GO

UPDATE dbo.products
SET description = ISNULL(description, short_description)
WHERE description IS NULL;
GO

ALTER TABLE dbo.products ALTER COLUMN description NVARCHAR(2000) NOT NULL;
GO

IF COL_LENGTH('dbo.products', 'image_url') IS NULL
BEGIN
    ALTER TABLE dbo.products ADD image_url NVARCHAR(500) NULL;
END
GO

UPDATE dbo.products
SET image_url = main_image_url
WHERE image_url IS NULL;
GO

DECLARE @sql NVARCHAR(MAX);

-- Drop indexes that reference legacy columns to avoid DROP COLUMN failures.
DECLARE legacy_index_cursor CURSOR FOR
SELECT DISTINCT i.name
FROM sys.indexes i
JOIN sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
JOIN sys.columns c ON c.object_id = ic.object_id AND c.column_id = ic.column_id
WHERE i.object_id = OBJECT_ID('dbo.products')
  AND c.name IN ('short_description', 'long_description', 'main_image_url', 'image_urls_json', 'attributes_json', 'variants_json', 'has_variants', 'is_visible', 'is_available')
  AND i.is_primary_key = 0
  AND i.is_unique_constraint = 0;

OPEN legacy_index_cursor;
FETCH NEXT FROM legacy_index_cursor INTO @sql;
WHILE @@FETCH_STATUS = 0
BEGIN
    EXEC ('DROP INDEX [' + @sql + '] ON dbo.products');
    FETCH NEXT FROM legacy_index_cursor INTO @sql;
END
CLOSE legacy_index_cursor;
DEALLOCATE legacy_index_cursor;
GO

-- Helper pattern per column: drop default/check constraints and then drop column if exists.
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);

-- short_description
IF COL_LENGTH('dbo.products', 'short_description') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'short_description';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN short_description;
END
GO

-- long_description
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'long_description') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'long_description';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN long_description;
END
GO

-- main_image_url
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'main_image_url') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'main_image_url';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN main_image_url;
END
GO

-- image_urls_json
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'image_urls_json') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'image_urls_json';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN image_urls_json;
END
GO

-- attributes_json
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'attributes_json') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'attributes_json';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN attributes_json;
END
GO

-- variants_json
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'variants_json') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'variants_json';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN variants_json;
END
GO

-- has_variants
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'has_variants') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'has_variants';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN has_variants;
END
GO

-- is_visible
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'is_visible') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'is_visible';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN is_visible;
END
GO

-- is_available
DECLARE @constraintName SYSNAME;
DECLARE @dropSql NVARCHAR(MAX);
IF COL_LENGTH('dbo.products', 'is_available') IS NOT NULL
BEGIN
    SELECT @constraintName = dc.name
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID('dbo.products') AND c.name = 'is_available';

    IF @constraintName IS NOT NULL
    BEGIN
        SET @dropSql = 'ALTER TABLE dbo.products DROP CONSTRAINT [' + @constraintName + ']';
        EXEC sp_executesql @dropSql;
    END

    ALTER TABLE dbo.products DROP COLUMN is_available;
END
GO
