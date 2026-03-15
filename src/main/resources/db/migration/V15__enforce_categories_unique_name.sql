IF OBJECT_ID('dbo.categories', 'U') IS NOT NULL
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM sys.indexes
        WHERE name = 'uq_categories_name'
          AND object_id = OBJECT_ID('dbo.categories')
    )
    BEGIN
        CREATE UNIQUE INDEX uq_categories_name ON dbo.categories(name);
    END;
END;
