IF OBJECT_ID('dbo.categories', 'U') IS NOT NULL
   AND COL_LENGTH('dbo.categories', 'description') IS NOT NULL
BEGIN
    ALTER TABLE dbo.categories
    DROP COLUMN description;
END;
