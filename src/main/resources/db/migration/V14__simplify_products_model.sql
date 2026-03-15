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

ALTER TABLE dbo.products DROP COLUMN short_description;
GO

IF COL_LENGTH('dbo.products', 'long_description') IS NOT NULL
BEGIN
    ALTER TABLE dbo.products DROP COLUMN long_description;
END
GO

ALTER TABLE dbo.products DROP COLUMN main_image_url;
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
