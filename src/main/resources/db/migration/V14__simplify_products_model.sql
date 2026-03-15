IF COL_LENGTH('dbo.products', 'description') IS NULL
BEGIN
    ALTER TABLE dbo.products ADD description NVARCHAR(2000) NULL;
END
GO

UPDATE dbo.products
SET description = ISNULL(description, short_description)
WHERE description IS NULL;
GO

UPDATE dbo.products
SET description = ''
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

DECLARE @dropConstraintSql NVARCHAR(MAX) = N'';

SELECT @dropConstraintSql = @dropConstraintSql +
       N'ALTER TABLE dbo.products DROP CONSTRAINT [' + dc.name + N'];' + CHAR(10)
FROM sys.default_constraints dc
INNER JOIN sys.columns c
        ON c.object_id = dc.parent_object_id
       AND c.column_id = dc.parent_column_id
WHERE dc.parent_object_id = OBJECT_ID(N'dbo.products')
  AND c.name IN (N'short_description', N'long_description', N'main_image_url', N'image_urls_json',
                 N'attributes_json', N'variants_json', N'has_variants', N'is_visible', N'is_available');

IF LEN(@dropConstraintSql) > 0
BEGIN
    EXEC sp_executesql @dropConstraintSql;
END
GO

IF COL_LENGTH('dbo.products', 'short_description') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN short_description;
END
GO

IF COL_LENGTH('dbo.products', 'long_description') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN long_description;
END
GO

IF COL_LENGTH('dbo.products', 'main_image_url') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN main_image_url;
END
GO

IF COL_LENGTH('dbo.products', 'image_urls_json') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN image_urls_json;
END
GO

IF COL_LENGTH('dbo.products', 'attributes_json') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN attributes_json;
END
GO

IF COL_LENGTH('dbo.products', 'variants_json') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN variants_json;
END
GO

IF COL_LENGTH('dbo.products', 'has_variants') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN has_variants;
END
GO

IF COL_LENGTH('dbo.products', 'is_visible') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN is_visible;
END
GO

IF COL_LENGTH('dbo.products', 'is_available') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN is_available;
END
GO
