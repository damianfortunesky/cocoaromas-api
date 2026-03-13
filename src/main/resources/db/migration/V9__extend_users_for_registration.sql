IF COL_LENGTH('users', 'is_active') IS NULL
BEGIN
    ALTER TABLE users ADD is_active BIT NOT NULL CONSTRAINT df_users_is_active DEFAULT 1;
END;

IF COL_LENGTH('users', 'created_at') IS NULL
BEGIN
    ALTER TABLE users ADD created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_users_created_at DEFAULT SYSDATETIMEOFFSET();
END;

IF COL_LENGTH('users', 'updated_at') IS NULL
BEGIN
    ALTER TABLE users ADD updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_users_updated_at DEFAULT SYSDATETIMEOFFSET();
END;

IF COL_LENGTH('users', 'name') IS NOT NULL
BEGIN
    ALTER TABLE users DROP COLUMN name;
END;

IF COL_LENGTH('users', 'username') IS NOT NULL
BEGIN
    ALTER TABLE users DROP COLUMN username;
END;

IF COL_LENGTH('users', 'first_name') IS NOT NULL
BEGIN
    ALTER TABLE users DROP COLUMN first_name;
END;

IF COL_LENGTH('users', 'last_name') IS NOT NULL
BEGIN
    ALTER TABLE users DROP COLUMN last_name;
END;
