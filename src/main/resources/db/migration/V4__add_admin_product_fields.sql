ALTER TABLE products
ADD is_active BIT NOT NULL CONSTRAINT df_products_is_active DEFAULT 1,
    is_available BIT NOT NULL CONSTRAINT df_products_is_available DEFAULT 1,
    deleted_at DATETIMEOFFSET NULL,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_products_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_products_updated_at DEFAULT SYSDATETIMEOFFSET();
GO

UPDATE products
SET is_active = is_visible,
    is_available = CASE WHEN stock_quantity > 0 THEN 1 ELSE 0 END;
GO

CREATE INDEX idx_products_deleted_at ON products(deleted_at);
CREATE INDEX idx_products_is_active ON products(is_active);
