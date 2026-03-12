INSERT INTO categories (slug, name, display_order)
VALUES
('sahumerios', 'Sahumerios', 1),
('perfumes', 'Perfumes', 2),
('remeras', 'Remeras', 3),
('collares', 'Collares', 4),
('adornos', 'Adornos', 5),
('jabones', 'Jabones', 6),
('articulos-de-limpieza', 'Artículos de limpieza', 7),
('shampoo-para-autos', 'Shampoo para autos', 8);

INSERT INTO products (name, short_description, price, category_id, main_image_url, stock_quantity, is_visible, has_variants, attributes_json)
VALUES
('Sahumerio Sagrada Madre Lavanda', 'Blend relajante de lavanda y hierbas.', 3500.00, 1, 'https://images.mock.cocoaromas/sahumerio-lavanda.jpg', 24, 1, 0, '{"marca":"Sagrada Madre","aroma":"Lavanda","presentacion":"Caja x8"}'),
('Sahumerio Nag Champa Clásico', 'Fragancia intensa y espiritual.', 2800.00, 1, 'https://images.mock.cocoaromas/sahumerio-nagchampa.jpg', 0, 1, 0, '{"marca":"Goloka","aroma":"Nag Champa","presentacion":"Caja x12"}'),
('Perfume Textil Coco Fresh', 'Aroma fresco para telas y ambientes.', 7900.00, 2, 'https://images.mock.cocoaromas/perfume-textil-fresh.jpg', 11, 1, 0, '{"marca":"Cocoaromas","fragancia":"Fresh Linen","contenido":"500ml"}'),
('Perfume de Ambiente Vainilla', 'Difusor con notas dulces y cálidas.', 6800.00, 2, 'https://images.mock.cocoaromas/perfume-vainilla.jpg', 3, 1, 0, '{"marca":"Cocoaromas","fragancia":"Vainilla","contenido":"250ml"}'),
('Remera Oversize Cocoaromas Negra', 'Remera unisex de algodón premium.', 16500.00, 3, 'https://images.mock.cocoaromas/remera-oversize-negra.jpg', 6, 1, 1, '{"material":"Algodón","color":"Negro"}'),
('Collar Piedra Luna', 'Collar artesanal con dije de piedra luna.', 12900.00, 4, 'https://images.mock.cocoaromas/collar-piedra-luna.jpg', 4, 1, 0, '{"material":"Acero quirúrgico","estilo":"Boho"}'),
('Adorno Buda Cerámica Blanca', 'Figura decorativa para armonización.', 9800.00, 5, 'https://images.mock.cocoaromas/adorno-buda-blanco.jpg', 0, 1, 0, '{"material":"Cerámica","tamano":"20cm"}'),
('Jabón Artesanal Coco y Karité', 'Jabón hidratante de uso diario.', 4200.00, 6, 'https://images.mock.cocoaromas/jabon-coco-karite.jpg', 18, 1, 0, '{"aroma":"Coco","peso":"120g"}'),
('Limpiador Multiuso Cítrico', 'Limpieza profunda con fragancia cítrica.', 5100.00, 7, 'https://images.mock.cocoaromas/limpiador-citrico.jpg', 9, 1, 0, '{"tipo":"Multiuso","contenido":"750ml"}'),
('Shampoo Auto pH Neutro', 'Shampoo espumoso para carrocería brillante.', 8900.00, 8, 'https://images.mock.cocoaromas/shampoo-auto-ph-neutro.jpg', 14, 1, 0, '{"marca":"AutoCare","contenido":"1L","tipo":"pH neutro"}');
