/*
Modelo SQL Server propuesto para ecommerce Cocoaromas.
- Mantiene entidades existentes (users, categories, products, orders, order_items, promotions)
- Agrega entidades operativas para escalabilidad ecommerce.
*/
USE [db_cocoaromas];
GO

IF OBJECT_ID('dbo.users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(120) NOT NULL,
        first_name NVARCHAR(60) NOT NULL,
        last_name NVARCHAR(60) NOT NULL,
        email NVARCHAR(120) NOT NULL UNIQUE,
        username NVARCHAR(80) NOT NULL UNIQUE,
        password_hash NVARCHAR(120) NOT NULL,
        role NVARCHAR(30) NOT NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET()
    );
END
GO

IF OBJECT_ID('dbo.categories', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.categories (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        slug NVARCHAR(120) NOT NULL UNIQUE,
        name NVARCHAR(120) NOT NULL,
        display_order INT NOT NULL DEFAULT 0
    );
END
GO

IF OBJECT_ID('dbo.products', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.products (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(160) NOT NULL,
        short_description NVARCHAR(280) NOT NULL,
        long_description NVARCHAR(2000) NULL,
        price DECIMAL(12,2) NOT NULL,
        category_id BIGINT NOT NULL,
        main_image_url NVARCHAR(500) NOT NULL,
        image_urls_json NVARCHAR(MAX) NULL,
        stock_quantity INT NOT NULL,
        is_visible BIT NOT NULL DEFAULT 1,
        has_variants BIT NOT NULL DEFAULT 0,
        attributes_json NVARCHAR(MAX) NULL,
        variants_json NVARCHAR(MAX) NULL,
        is_active BIT NOT NULL DEFAULT 1,
        is_available BIT NOT NULL DEFAULT 1,
        deleted_at DATETIMEOFFSET NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES dbo.categories(id)
    );

    CREATE INDEX idx_products_name ON dbo.products(name);
    CREATE INDEX idx_products_category ON dbo.products(category_id);
    CREATE INDEX idx_products_visible ON dbo.products(is_visible);
    CREATE INDEX idx_products_deleted_at ON dbo.products(deleted_at);
    CREATE INDEX idx_products_is_active ON dbo.products(is_active);
END
GO

IF OBJECT_ID('dbo.user_addresses', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.user_addresses (
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
        state NVARCHAR(120) NULL,
        postal_code NVARCHAR(20) NOT NULL,
        country_code NVARCHAR(2) NOT NULL,
        reference NVARCHAR(300) NULL,
        is_default_shipping BIT NOT NULL DEFAULT 0,
        is_default_billing BIT NOT NULL DEFAULT 0,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES dbo.users(id)
    );

    CREATE INDEX idx_user_addresses_user_id ON dbo.user_addresses(user_id);
END
GO

IF OBJECT_ID('dbo.orders', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.orders (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id BIGINT NOT NULL,
        status NVARCHAR(40) NOT NULL,
        payment_method NVARCHAR(40) NOT NULL,
        payment_status NVARCHAR(30) NOT NULL DEFAULT 'PENDING',
        notes NVARCHAR(1000) NULL,
        delivery_method NVARCHAR(100) NULL,
        currency_code NVARCHAR(3) NOT NULL DEFAULT 'ARS',
        subtotal DECIMAL(12,2) NOT NULL,
        discount_total DECIMAL(12,2) NOT NULL DEFAULT 0,
        shipping_total DECIMAL(12,2) NOT NULL DEFAULT 0,
        total DECIMAL(12,2) NOT NULL,
        billing_address_id BIGINT NULL,
        shipping_address_id BIGINT NULL,
        status_reason NVARCHAR(500) NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NULL,
        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES dbo.users(id),
        CONSTRAINT fk_orders_billing_address FOREIGN KEY (billing_address_id) REFERENCES dbo.user_addresses(id),
        CONSTRAINT fk_orders_shipping_address FOREIGN KEY (shipping_address_id) REFERENCES dbo.user_addresses(id)
    );

    CREATE INDEX idx_orders_user_id ON dbo.orders(user_id);
    CREATE INDEX idx_orders_status ON dbo.orders(status);
    CREATE INDEX idx_orders_created_at ON dbo.orders(created_at);
    CREATE INDEX idx_orders_payment_method ON dbo.orders(payment_method);
    CREATE INDEX idx_orders_payment_status ON dbo.orders(payment_status);
END
GO

IF OBJECT_ID('dbo.product_variants', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.product_variants (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        product_id BIGINT NOT NULL,
        sku NVARCHAR(80) NOT NULL UNIQUE,
        name NVARCHAR(160) NOT NULL,
        attributes_json NVARCHAR(MAX) NULL,
        price_override DECIMAL(12,2) NULL,
        stock_quantity INT NOT NULL DEFAULT 0,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id) REFERENCES dbo.products(id)
    );
END
GO

IF OBJECT_ID('dbo.order_items', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.order_items (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        order_id BIGINT NOT NULL,
        product_id BIGINT NOT NULL,
        product_variant_id BIGINT NULL,
        product_name NVARCHAR(160) NOT NULL,
        variant_id NVARCHAR(80) NULL,
        quantity INT NOT NULL,
        unit_price DECIMAL(12,2) NOT NULL,
        discount_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
        tax_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
        subtotal DECIMAL(12,2) NOT NULL,
        total_amount DECIMAL(12,2) NOT NULL,
        CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES dbo.orders(id),
        CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES dbo.products(id),
        CONSTRAINT fk_order_items_variant FOREIGN KEY (product_variant_id) REFERENCES dbo.product_variants(id)
    );

    CREATE INDEX idx_order_items_order_id ON dbo.order_items(order_id);
    CREATE INDEX idx_order_items_variant_id ON dbo.order_items(product_variant_id);
END
GO

IF OBJECT_ID('dbo.carts', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.carts (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id BIGINT NOT NULL,
        status NVARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
        currency_code NVARCHAR(3) NOT NULL DEFAULT 'ARS',
        expires_at DATETIMEOFFSET NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES dbo.users(id)
    );
END
GO

IF OBJECT_ID('dbo.cart_items', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.cart_items (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        cart_id BIGINT NOT NULL,
        product_id BIGINT NOT NULL,
        product_variant_id BIGINT NULL,
        quantity INT NOT NULL,
        unit_price DECIMAL(12,2) NOT NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES dbo.carts(id),
        CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES dbo.products(id),
        CONSTRAINT fk_cart_items_variant FOREIGN KEY (product_variant_id) REFERENCES dbo.product_variants(id)
    );
END
GO

IF OBJECT_ID('dbo.promotions', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.promotions (
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
        CONSTRAINT fk_promotions_product FOREIGN KEY (product_id) REFERENCES dbo.products(id),
        CONSTRAINT fk_promotions_category FOREIGN KEY (category_id) REFERENCES dbo.categories(id)
    );
END
GO

IF OBJECT_ID('dbo.payments', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.payments (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        order_id BIGINT NOT NULL,
        provider NVARCHAR(80) NOT NULL,
        provider_reference NVARCHAR(120) NULL,
        payment_method NVARCHAR(40) NOT NULL,
        status NVARCHAR(30) NOT NULL,
        amount DECIMAL(12,2) NOT NULL,
        currency_code NVARCHAR(3) NOT NULL,
        paid_at DATETIMEOFFSET NULL,
        failure_reason NVARCHAR(600) NULL,
        metadata_json NVARCHAR(MAX) NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES dbo.orders(id)
    );
END
GO

IF OBJECT_ID('dbo.shipments', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.shipments (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        order_id BIGINT NOT NULL,
        shipping_method NVARCHAR(80) NOT NULL,
        carrier NVARCHAR(120) NULL,
        tracking_number NVARCHAR(120) NULL,
        status NVARCHAR(30) NOT NULL,
        shipped_at DATETIMEOFFSET NULL,
        delivered_at DATETIMEOFFSET NULL,
        shipping_cost DECIMAL(12,2) NOT NULL DEFAULT 0,
        notes NVARCHAR(500) NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_shipments_order FOREIGN KEY (order_id) REFERENCES dbo.orders(id)
    );
END
GO

IF OBJECT_ID('dbo.inventory_movements', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.inventory_movements (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        product_id BIGINT NOT NULL,
        product_variant_id BIGINT NULL,
        movement_type NVARCHAR(30) NOT NULL,
        quantity INT NOT NULL,
        reference_type NVARCHAR(40) NULL,
        reference_id BIGINT NULL,
        notes NVARCHAR(500) NULL,
        created_by_user_id BIGINT NULL,
        created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
        CONSTRAINT fk_inventory_movements_product FOREIGN KEY (product_id) REFERENCES dbo.products(id),
        CONSTRAINT fk_inventory_movements_variant FOREIGN KEY (product_variant_id) REFERENCES dbo.product_variants(id),
        CONSTRAINT fk_inventory_movements_user FOREIGN KEY (created_by_user_id) REFERENCES dbo.users(id)
    );
END
GO
