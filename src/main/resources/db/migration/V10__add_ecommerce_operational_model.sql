/*
  Extiende el modelo actual sin romper compatibilidad con tablas existentes.
  Agrega soporte de ecommerce para:
  - direcciones de usuario y snapshot de envío/facturación por pedido
  - carrito de compra persistente
  - variantes e imágenes de productos normalizadas
  - pagos, envíos e historial de estados
  - movimientos de inventario
*/

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
    state NVARCHAR(120) NULL,
    postal_code NVARCHAR(20) NOT NULL,
    country_code NVARCHAR(2) NOT NULL,
    reference NVARCHAR(300) NULL,
    is_default_shipping BIT NOT NULL DEFAULT 0,
    is_default_billing BIT NOT NULL DEFAULT 0,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT ck_user_addresses_country_code_len CHECK (LEN(country_code) = 2)
);

CREATE INDEX idx_user_addresses_user_id ON user_addresses(user_id);
CREATE INDEX idx_user_addresses_user_default_shipping ON user_addresses(user_id, is_default_shipping);
CREATE INDEX idx_user_addresses_user_default_billing ON user_addresses(user_id, is_default_billing);

CREATE TABLE product_images (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_url NVARCHAR(500) NOT NULL,
    is_primary BIT NOT NULL DEFAULT 0,
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE UNIQUE INDEX uq_product_images_primary_per_product
ON product_images(product_id, is_primary)
WHERE is_primary = 1;

CREATE INDEX idx_product_images_product_id_order ON product_images(product_id, display_order);

CREATE TABLE product_variants (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku NVARCHAR(80) NOT NULL,
    name NVARCHAR(160) NOT NULL,
    attributes_json NVARCHAR(MAX) NULL,
    price_override DECIMAL(12,2) NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uq_product_variants_sku UNIQUE (sku),
    CONSTRAINT ck_product_variants_stock CHECK (stock_quantity >= 0)
);

CREATE INDEX idx_product_variants_product_id ON product_variants(product_id);
CREATE INDEX idx_product_variants_active ON product_variants(is_active);

CREATE TABLE carts (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status NVARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    currency_code NVARCHAR(3) NOT NULL DEFAULT 'ARS',
    expires_at DATETIMEOFFSET NULL,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT ck_carts_status CHECK (status IN ('ACTIVE', 'ABANDONED', 'CONVERTED', 'EXPIRED'))
);

CREATE UNIQUE INDEX uq_carts_active_per_user
ON carts(user_id, status)
WHERE status = 'ACTIVE';

CREATE INDEX idx_carts_user_id ON carts(user_id);

CREATE TABLE cart_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_variant_id BIGINT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id),
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_cart_items_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id),
    CONSTRAINT ck_cart_items_quantity CHECK (quantity > 0),
    CONSTRAINT uq_cart_items_product_variant UNIQUE (cart_id, product_id, product_variant_id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);

ALTER TABLE orders
ADD currency_code NVARCHAR(3) NULL,
    subtotal DECIMAL(12,2) NULL,
    discount_total DECIMAL(12,2) NULL,
    shipping_total DECIMAL(12,2) NULL,
    billing_address_id BIGINT NULL,
    shipping_address_id BIGINT NULL,
    payment_status NVARCHAR(30) NULL;

UPDATE orders
SET currency_code = ISNULL(currency_code, 'ARS'),
    subtotal = ISNULL(subtotal, total),
    discount_total = ISNULL(discount_total, 0),
    shipping_total = ISNULL(shipping_total, 0),
    payment_status = ISNULL(payment_status, 'PENDING');

ALTER TABLE orders ALTER COLUMN currency_code NVARCHAR(3) NOT NULL;
ALTER TABLE orders ALTER COLUMN subtotal DECIMAL(12,2) NOT NULL;
ALTER TABLE orders ALTER COLUMN discount_total DECIMAL(12,2) NOT NULL;
ALTER TABLE orders ALTER COLUMN shipping_total DECIMAL(12,2) NOT NULL;
ALTER TABLE orders ALTER COLUMN payment_status NVARCHAR(30) NOT NULL;

ALTER TABLE orders
ADD CONSTRAINT fk_orders_billing_address FOREIGN KEY (billing_address_id) REFERENCES user_addresses(id),
    CONSTRAINT fk_orders_shipping_address FOREIGN KEY (shipping_address_id) REFERENCES user_addresses(id),
    CONSTRAINT ck_orders_currency_code_len CHECK (LEN(currency_code) = 3),
    CONSTRAINT ck_orders_payment_status CHECK (payment_status IN ('PENDING', 'AUTHORIZED', 'PAID', 'REJECTED', 'REFUNDED', 'PARTIALLY_REFUNDED')),
    CONSTRAINT ck_orders_amounts_non_negative CHECK (subtotal >= 0 AND discount_total >= 0 AND shipping_total >= 0 AND total >= 0);

CREATE INDEX idx_orders_payment_status ON orders(payment_status);

ALTER TABLE order_items
ADD product_variant_id BIGINT NULL,
    discount_amount DECIMAL(12,2) NULL,
    tax_amount DECIMAL(12,2) NULL,
    total_amount DECIMAL(12,2) NULL;

UPDATE order_items
SET discount_amount = ISNULL(discount_amount, 0),
    tax_amount = ISNULL(tax_amount, 0),
    total_amount = ISNULL(total_amount, subtotal);

ALTER TABLE order_items ALTER COLUMN discount_amount DECIMAL(12,2) NOT NULL;
ALTER TABLE order_items ALTER COLUMN tax_amount DECIMAL(12,2) NOT NULL;
ALTER TABLE order_items ALTER COLUMN total_amount DECIMAL(12,2) NOT NULL;

ALTER TABLE order_items
ADD CONSTRAINT fk_order_items_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id),
    CONSTRAINT ck_order_items_amounts_non_negative CHECK (discount_amount >= 0 AND tax_amount >= 0 AND total_amount >= 0);

CREATE INDEX idx_order_items_variant_id ON order_items(product_variant_id);

CREATE TABLE order_status_history (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    previous_status NVARCHAR(40) NULL,
    new_status NVARCHAR(40) NOT NULL,
    reason NVARCHAR(500) NULL,
    changed_by_user_id BIGINT NULL,
    changed_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_order_status_history_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_status_history_user FOREIGN KEY (changed_by_user_id) REFERENCES users(id)
);

CREATE INDEX idx_order_status_history_order_id ON order_status_history(order_id, changed_at DESC);

CREATE TABLE payments (
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
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT ck_payments_status CHECK (status IN ('PENDING', 'AUTHORIZED', 'PAID', 'FAILED', 'CANCELLED', 'REFUNDED', 'PARTIALLY_REFUNDED')),
    CONSTRAINT ck_payments_amount CHECK (amount >= 0),
    CONSTRAINT ck_payments_currency_code_len CHECK (LEN(currency_code) = 3)
);

CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE UNIQUE INDEX uq_payments_provider_reference ON payments(provider, provider_reference) WHERE provider_reference IS NOT NULL;

CREATE TABLE shipments (
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
    CONSTRAINT fk_shipments_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT ck_shipments_status CHECK (status IN ('PENDING', 'PREPARING', 'SHIPPED', 'DELIVERED', 'RETURNED', 'CANCELLED')),
    CONSTRAINT ck_shipments_cost CHECK (shipping_cost >= 0)
);

CREATE INDEX idx_shipments_order_id ON shipments(order_id);
CREATE INDEX idx_shipments_tracking_number ON shipments(tracking_number);

CREATE TABLE inventory_movements (
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
    CONSTRAINT fk_inventory_movements_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_inventory_movements_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id),
    CONSTRAINT fk_inventory_movements_user FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    CONSTRAINT ck_inventory_movements_type CHECK (movement_type IN ('IN', 'OUT', 'RESERVED', 'RELEASED', 'ADJUSTMENT')),
    CONSTRAINT ck_inventory_movements_quantity_non_zero CHECK (quantity <> 0)
);

CREATE INDEX idx_inventory_movements_product ON inventory_movements(product_id, created_at DESC);
CREATE INDEX idx_inventory_movements_variant ON inventory_movements(product_variant_id, created_at DESC);
