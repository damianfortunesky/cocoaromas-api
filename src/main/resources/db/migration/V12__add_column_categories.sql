IF OBJECT_ID('dbo.categories', 'U') IS NOT NULL
   AND COL_LENGTH('dbo.categories', 'description') IS NULL
BEGIN
    ALTER TABLE dbo.categories
    ADD description NVARCHAR(500) NULL;
END;
