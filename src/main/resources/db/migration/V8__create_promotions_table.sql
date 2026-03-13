CREATE TABLE promotions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(160) NOT NULL,
    description NVARCHAR(1200) NULL,
    promotion_type NVARCHAR(20) NOT NULL,
    discount_type NVARCHAR(20) NOT NULL,
    discount_value DECIMAL(12,2) NOT NULL,
    minimum_quantity INT NULL,
    product_id BIGINT NULL,
    category_id BIGINT NULL,
    is_active BIT NOT NULL DEFAULT 1,
    starts_at DATETIMEOFFSET NULL,
    ends_at DATETIMEOFFSET NULL,
    deleted_at DATETIMEOFFSET NULL,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_promotions_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_promotions_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_promotions_active ON promotions(is_active);
CREATE INDEX idx_promotions_type ON promotions(promotion_type);
CREATE INDEX idx_promotions_dates ON promotions(starts_at, ends_at);
CREATE INDEX idx_promotions_deleted_at ON promotions(deleted_at);
