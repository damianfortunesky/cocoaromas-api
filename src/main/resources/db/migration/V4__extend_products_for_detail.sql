ALTER TABLE products
ADD long_description NVARCHAR(2000) NULL,
    image_urls_json NVARCHAR(MAX) NULL,
    variants_json NVARCHAR(MAX) NULL;

UPDATE products
SET long_description = 'Blend artesanal con varillas de combustión lenta y notas herbales de lavanda. Ideal para rituales de relajación, meditación o armonización de ambientes.',
    image_urls_json = '["https://images.mock.cocoaromas/sahumerio-lavanda.jpg","https://images.mock.cocoaromas/sahumerio-lavanda-2.jpg","https://images.mock.cocoaromas/sahumerio-lavanda-3.jpg"]'
WHERE name = 'Sahumerio Sagrada Madre Lavanda';

UPDATE products
SET long_description = 'Fragancia clásica de origen oriental. Su aroma intenso y profundo acompaña prácticas espirituales y perfuma ambientes por horas.',
    image_urls_json = '["https://images.mock.cocoaromas/sahumerio-nagchampa.jpg","https://images.mock.cocoaromas/sahumerio-nagchampa-2.jpg"]'
WHERE name = 'Sahumerio Nag Champa Clásico';

UPDATE products
SET long_description = 'Bruma textil diseñada para renovar prendas, cortinas y sillones. Notas limpias y duraderas sin manchar los tejidos.',
    image_urls_json = '["https://images.mock.cocoaromas/perfume-textil-fresh.jpg","https://images.mock.cocoaromas/perfume-textil-fresh-2.jpg"]'
WHERE name = 'Perfume Textil Coco Fresh';

UPDATE products
SET long_description = 'Remera oversize con calce relajado. Tela suave de algodón premium y costuras reforzadas para uso diario.',
    image_urls_json = '["https://images.mock.cocoaromas/remera-oversize-negra.jpg","https://images.mock.cocoaromas/remera-oversize-negra-2.jpg","https://images.mock.cocoaromas/remera-oversize-negra-3.jpg"]',
    variants_json = '[{"id":"REM-OVR-BLK-S","name":"Talle S","attributes":{"talle":"S","color":"Negro"},"stockQuantity":2,"available":true},{"id":"REM-OVR-BLK-M","name":"Talle M","attributes":{"talle":"M","color":"Negro"},"stockQuantity":0,"available":false},{"id":"REM-OVR-BLK-L","name":"Talle L","attributes":{"talle":"L","color":"Negro"},"stockQuantity":4,"available":true}]'
WHERE name = 'Remera Oversize Cocoaromas Negra';

UPDATE products
SET long_description = 'Figura decorativa de cerámica blanca para espacios de calma y bienestar. Terminación mate y base estable.',
    image_urls_json = '["https://images.mock.cocoaromas/adorno-buda-blanco.jpg","https://images.mock.cocoaromas/adorno-buda-blanco-2.jpg"]'
WHERE name = 'Adorno Buda Cerámica Blanca';

UPDATE products
SET long_description = 'Jabón vegetal elaborado con coco y manteca de karité. Limpia de forma suave y deja la piel humectada.',
    image_urls_json = '["https://images.mock.cocoaromas/jabon-coco-karite.jpg","https://images.mock.cocoaromas/jabon-coco-karite-2.jpg"]'
WHERE name = 'Jabón Artesanal Coco y Karité';

UPDATE products
SET long_description = 'Shampoo espumoso para limpieza de carrocería con pH neutro. Ayuda a conservar ceras y selladores.',
    image_urls_json = '["https://images.mock.cocoaromas/shampoo-auto-ph-neutro.jpg","https://images.mock.cocoaromas/shampoo-auto-ph-neutro-2.jpg"]'
WHERE name = 'Shampoo Auto pH Neutro';
