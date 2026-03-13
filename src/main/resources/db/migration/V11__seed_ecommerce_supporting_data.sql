/*
  Backfill inicial para nuevas estructuras normalizadas.
  - product_images: imagen principal para todos los productos existentes
  - order_status_history: snapshot del estado actual de pedidos existentes
  - inventory_movements: snapshot de stock actual por producto
*/

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id, p.main_image_url, 1, 0
FROM products p
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images pi
    WHERE pi.product_id = p.id
      AND pi.is_primary = 1
);

INSERT INTO order_status_history (order_id, previous_status, new_status, reason, changed_by_user_id, changed_at)
SELECT o.id,
       NULL,
       o.status,
       'Estado inicial migrado desde orders.status',
       NULL,
       ISNULL(o.updated_at, o.created_at)
FROM orders o
WHERE NOT EXISTS (
    SELECT 1
    FROM order_status_history osh
    WHERE osh.order_id = o.id
);

INSERT INTO inventory_movements (product_id, product_variant_id, movement_type, quantity, reference_type, reference_id, notes, created_by_user_id, created_at)
SELECT p.id,
       NULL,
       'ADJUSTMENT',
       p.stock_quantity,
       'MIGRATION',
       p.id,
       'Stock inicial migrado desde products.stock_quantity',
       NULL,
       SYSDATETIMEOFFSET()
FROM products p
WHERE NOT EXISTS (
    SELECT 1
    FROM inventory_movements im
    WHERE im.product_id = p.id
      AND im.reference_type = 'MIGRATION'
      AND im.reference_id = p.id
);
