ALTER TABLE users ADD first_name NVARCHAR(60) NULL;
ALTER TABLE users ADD last_name NVARCHAR(60) NULL;
ALTER TABLE users ADD created_at DATETIMEOFFSET NULL;

UPDATE users
SET first_name = CASE
        WHEN CHARINDEX(' ', name) > 0 THEN LEFT(name, CHARINDEX(' ', name) - 1)
        ELSE name
    END,
    last_name = CASE
        WHEN CHARINDEX(' ', name) > 0 THEN LTRIM(SUBSTRING(name, CHARINDEX(' ', name) + 1, LEN(name)))
        ELSE name
    END,
    created_at = SYSDATETIMEOFFSET()
WHERE first_name IS NULL OR last_name IS NULL OR created_at IS NULL;

ALTER TABLE users ALTER COLUMN first_name NVARCHAR(60) NOT NULL;
ALTER TABLE users ALTER COLUMN last_name NVARCHAR(60) NOT NULL;
ALTER TABLE users ALTER COLUMN created_at DATETIMEOFFSET NOT NULL;
