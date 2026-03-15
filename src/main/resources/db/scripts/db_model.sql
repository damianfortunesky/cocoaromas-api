CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(120) NOT NULL,
    password_hash NVARCHAR(120) NOT NULL,
    role_name NVARCHAR(30) NOT NULL,
    is_active BIT NOT NULL CONSTRAINT df_users_is_active DEFAULT 1,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_users_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_users_updated_at DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT uq_users_email UNIQUE (email)
);
GO

CREATE TABLE categories (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    category_name NVARCHAR(120) NOT NULL,
    slug NVARCHAR(120) NOT NULL,
    display_order INT NOT NULL CONSTRAINT df_categories_display_order DEFAULT 0,
    CONSTRAINT uq_categories_name UNIQUE (category_name),
    CONSTRAINT uq_categories_slug UNIQUE (slug)
);
GO

CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_name NVARCHAR(160) NOT NULL,
    product_description NVARCHAR(2000) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    category_id BIGINT NOT NULL,
    stock_quantity INT NOT NULL,
    image_url NVARCHAR(500) NULL,
    is_active BIT NOT NULL CONSTRAINT df_products_is_active DEFAULT 1,
    deleted_at DATETIMEOFFSET NULL,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_products_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_products_updated_at DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT ck_products_stock_quantity_non_negative CHECK (stock_quantity >= 0),
    CONSTRAINT ck_products_price_non_negative CHECK (price >= 0)
);
GO

CREATE TABLE user_details (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    first_name NVARCHAR(80) NULL,
    last_name NVARCHAR(80) NULL,
    phone NVARCHAR(40) NULL,
    dni NVARCHAR(40) NULL,
    birth_date DATE NULL,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_user_details_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_user_details_updated_at DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_user_details_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_user_details_user UNIQUE (user_id)
);
GO

CREATE TABLE user_addresses (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label NVARCHAR(80) NOT NULL,
    receiver_name NVARCHAR(140) NOT NULL,
    receiver_phone NVARCHAR(40) NULL,
    street NVARCHAR(160) NOT NULL,
    street_number NVARCHAR(20) NULL,
    floor NVARCHAR(20) NULL,
    apartment NVARCHAR(20) NULL,
    city NVARCHAR(120) NOT NULL,
    state_name NVARCHAR(120) NULL,
    postal_code NVARCHAR(20) NOT NULL,
    country_code NVARCHAR(2) NOT NULL,
    reference NVARCHAR(300) NULL,
    is_default_shipping BIT NOT NULL CONSTRAINT df_user_addresses_default_shipping DEFAULT 0,
    is_default_billing BIT NOT NULL CONSTRAINT df_user_addresses_default_billing DEFAULT 0,
    is_active BIT NOT NULL CONSTRAINT df_user_addresses_active DEFAULT 1,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_user_addresses_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_user_addresses_updated_at DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT ck_user_addresses_country_code_len CHECK (LEN(country_code) = 2)
);
GO

CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status_order NVARCHAR(40) NOT NULL,
    payment_method NVARCHAR(40) NOT NULL,
    notes NVARCHAR(1000) NULL,
    delivery_method NVARCHAR(100) NULL,
    total DECIMAL(12,2) NOT NULL,
    currency_code NVARCHAR(3) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    discount_total DECIMAL(12,2) NOT NULL,
    shipping_total DECIMAL(12,2) NOT NULL,
    payment_status NVARCHAR(30) NOT NULL,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_orders_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NULL,
    status_reason NVARCHAR(500) NULL,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT ck_orders_currency_code_len CHECK (LEN(currency_code) = 3),
    CONSTRAINT ck_orders_amounts_non_negative CHECK (subtotal >= 0 AND discount_total >= 0 AND shipping_total >= 0 AND total >= 0)
);
GO

CREATE TABLE order_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name NVARCHAR(160) NOT NULL,
    variant_id NVARCHAR(80) NULL,
    product_variant_id BIGINT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) NOT NULL,
    tax_amount DECIMAL(12,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT ck_order_items_quantity_positive CHECK (quantity > 0),
    CONSTRAINT ck_order_items_amounts_non_negative CHECK (unit_price >= 0 AND subtotal >= 0 AND discount_amount >= 0 AND tax_amount >= 0 AND total_amount >= 0)
);
GO

CREATE TABLE promotions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    promotion_name NVARCHAR(160) NOT NULL,
    promotion_description NVARCHAR(1200) NULL,
    promotion_type NVARCHAR(20) NOT NULL,
    discount_type NVARCHAR(20) NOT NULL,
    discount_value DECIMAL(12,2) NOT NULL,
    minimum_quantity INT NULL,
    product_id BIGINT NULL,
    category_id BIGINT NULL,
    is_active BIT NOT NULL CONSTRAINT df_promotions_is_active DEFAULT 1,
    starts_at DATETIMEOFFSET NULL,
    ends_at DATETIMEOFFSET NULL,
    deleted_at DATETIMEOFFSET NULL,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_promotions_created_at DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL CONSTRAINT df_promotions_updated_at DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_promotions_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_promotions_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT ck_promotions_discount_value_non_negative CHECK (discount_value >= 0)
);
GO

CREATE TABLE inventory_movements (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL,
    movement_type NVARCHAR(30) NOT NULL,
    quantity INT NOT NULL,
    reference_type NVARCHAR(40) NULL,
    notes NVARCHAR(500) NULL,
    created_by_user_id BIGINT NULL,
    created_at DATETIMEOFFSET NOT NULL CONSTRAINT df_inventory_movements_created_at DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_inventory_movements_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_inventory_movements_user FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    CONSTRAINT ck_inventory_movements_quantity_non_zero CHECK (quantity <> 0)
);
GO

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_products_deleted_at ON products(deleted_at);

CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_payment_method ON orders(payment_method);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

CREATE INDEX idx_user_addresses_user_id ON user_addresses(user_id);
CREATE INDEX idx_user_addresses_user_default_shipping ON user_addresses(user_id, is_default_shipping);
CREATE INDEX idx_user_addresses_user_default_billing ON user_addresses(user_id, is_default_billing);

CREATE INDEX idx_user_details_user_id ON user_details(user_id);

CREATE INDEX idx_promotions_active ON promotions(is_active);
CREATE INDEX idx_promotions_type ON promotions(promotion_type);
CREATE INDEX idx_promotions_dates ON promotions(starts_at, ends_at);
CREATE INDEX idx_promotions_deleted_at ON promotions(deleted_at);

CREATE INDEX idx_inventory_movements_product ON inventory_movements(product_id, created_at DESC);
CREATE INDEX idx_inventory_movements_user ON inventory_movements(created_by_user_id, created_at DESC);
