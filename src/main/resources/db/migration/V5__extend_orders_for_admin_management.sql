IF OBJECT_ID('dbo.orders', 'U') IS NOT NULL
BEGIN
    IF COL_LENGTH('dbo.orders', 'updated_at') IS NULL
    BEGIN
        ALTER TABLE dbo.orders ADD updated_at DATETIMEOFFSET NULL;
    END;

    IF COL_LENGTH('dbo.orders', 'status_reason') IS NULL
    BEGIN
        ALTER TABLE dbo.orders ADD status_reason NVARCHAR(500) NULL;
    END;

    IF NOT EXISTS (
        SELECT 1
        FROM sys.indexes
        WHERE name = 'idx_orders_created_at'
          AND object_id = OBJECT_ID('dbo.orders')
    )
    BEGIN
        CREATE INDEX idx_orders_created_at ON dbo.orders(created_at);
    END;

    IF NOT EXISTS (
        SELECT 1
        FROM sys.indexes
        WHERE name = 'idx_orders_payment_method'
          AND object_id = OBJECT_ID('dbo.orders')
    )
    BEGIN
        CREATE INDEX idx_orders_payment_method ON dbo.orders(payment_method);
    END;
END;
