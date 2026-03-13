CREATE TABLE categories (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    slug NVARCHAR(120) NOT NULL UNIQUE,
    name NVARCHAR(120) NOT NULL,
    display_order INT NOT NULL DEFAULT 0
);
GO

CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(160) NOT NULL,
    short_description NVARCHAR(280) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    category_id BIGINT NOT NULL,
    main_image_url NVARCHAR(500) NOT NULL,
    stock_quantity INT NOT NULL,
    is_visible BIT NOT NULL DEFAULT 1,
    has_variants BIT NOT NULL DEFAULT 0,
    attributes_json NVARCHAR(MAX) NULL,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
GO

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_visible ON products(is_visible);
