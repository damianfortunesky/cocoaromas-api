IF OBJECT_ID('dbo.products', 'U') IS NOT NULL
BEGIN
    IF COL_LENGTH('dbo.products', 'long_description') IS NULL
    BEGIN
        ALTER TABLE dbo.products ADD long_description NVARCHAR(2000) NULL;
    END;

    IF COL_LENGTH('dbo.products', 'image_urls_json') IS NULL
    BEGIN
        ALTER TABLE dbo.products ADD image_urls_json NVARCHAR(MAX) NULL;
    END;

    IF COL_LENGTH('dbo.products', 'variants_json') IS NULL
    BEGIN
        ALTER TABLE dbo.products ADD variants_json NVARCHAR(MAX) NULL;
    END;
END;
