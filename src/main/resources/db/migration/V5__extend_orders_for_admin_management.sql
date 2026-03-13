ALTER TABLE orders ADD updated_at DATETIMEOFFSET NULL;
ALTER TABLE orders ADD status_reason NVARCHAR(500) NULL;

CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_payment_method ON orders(payment_method);
