-- ==================================================================================
-- LIMPIEZA DE DATOS (REINICIO TOTAL)
-- ==================================================================================
TRUNCATE TABLE product_image CASCADE;
TRUNCATE TABLE product_variant CASCADE;
TRUNCATE TABLE product CASCADE;
TRUNCATE TABLE category CASCADE;
TRUNCATE TABLE order_status CASCADE;

-- ==================================================================================
-- ESTADOS DE PEDIDO
INSERT INTO order_status (id, code, description) VALUES
(1, 'QUOTE', 'Cotización'),
(2, 'CREATED', 'Pedido creado, aún no pagado'),
(3, 'CANCELLED', 'Pedido cancelado'),
(4, 'ACUMULANDO', 'En acumulación'),
(5, 'PENDIENTE_PACKAGE', 'Pendiente de empaquetado'),
(6, 'PENDIENTE_ENVIO', 'Pendiente de envío'),
(7, 'ENVIADO', 'Enviado'),
(8, 'PAID', 'Pagado');

-- ==================================================================================
-- INSERTAR CATEGORÍAS
-- Basado en las imágenes proporcionadas para "categories"
-- ==================================================================================
INSERT INTO category (id, name, image) VALUES
(3, 'Skincare', 'http://localhost:8000/images/5/file'),
(8, 'Polvos, corrector y bases', 'http://localhost:8000/images/5/file'),
(9, 'Rimel, delineador y aplicador para cejas', 'http://localhost:8000/images/5/file');

-- ==================================================================================
-- INSERTAR PRODUCTOS
-- ==================================================================================
INSERT INTO product (id, name, description, full_description, active, category_id) VALUES
(1, 'Base Cushion - Gagk', 'Base cushion coreana con cobertura ligera a media.', 'Base cushion coreana con cobertura ligera a media que unifica el tono de la piel dejando un acabo natural y luminoso.', true, 8),
(2, 'Brown Set ceja - Revel', 'Define y fija las cejas con un acabado natural.', 'Define y fija las cejas con un acabado natural, manteniéndolas prolijas todo el día.', true, 9),
(3, 'Corrector - Kevin & Coco', 'Cobertura impecable y acabado natural.', 'Ideal para camuflar imperfecciones sin resecar. Difumina poros y textura dejando la piel suave y uniforme.', true, 8),
(4, 'Crema Facial de día - Kevin & coco', 'Cobertura ligera con efecto "segunda piel".', 'Crema hidratante diaria que aporta luminosidad y protección.', true, 3);

-- ==================================================================================
-- INSERTAR PRODUCT VARIANTS (Precios y Stock base)
-- ==================================================================================
INSERT INTO product_variant (product_id, tone_name, tone_code, price, stock) VALUES
(1, 'Vanilla', '#F0DDC8', 18.00, 50),
(2, 'Natural', '#8F675C', 5.00, 30),
(3, 'Beige', '#F1E3C1', 7.00, 40),
(4, 'Día', '#ffffff', 15.00, 20);

-- ==================================================================================
-- INSERTAR IMÁGENES DE PRODUCTOS (SEGÚN NUEVA LISTA)
-- ==================================================================================
-- P1: Base Gagk
INSERT INTO product_image (product_id, url, position, main_image) VALUES
(1, 'http://localhost:8000/images/5/file', 1, true),
(1, 'http://localhost:8000/images/5/file', 2, false);
-- P2: Brown Revel
INSERT INTO product_image (product_id, url, position, main_image) VALUES
(2, 'http://localhost:8000/images/5/file', 1, true),
(2, 'http://localhost:8000/images/5/file', 2, false);
-- P3: Corrector KC
INSERT INTO product_image (product_id, url, position, main_image) VALUES
(3, 'http://localhost:8000/images/5/file', 1, true),
(3, 'http://localhost:8000/images/5/file', 2, false);
-- P4: Crema Hidratante
INSERT INTO product_image (product_id, url, position, main_image) VALUES
(4, 'http://localhost:8000/images/5/file', 1, true),
(4, 'http://localhost:8000/images/5/file', 2, false),
(4, 'http://localhost:8000/images/5/file', 3, false);

-- ==================================================================================
-- REINICIAR SECUENCIAS
-- ==================================================================================
SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));
SELECT setval('product_id_seq', (SELECT MAX(id) FROM product));
SELECT setval('product_variant_id_seq', (SELECT MAX(id) FROM product_variant));
SELECT setval('product_image_id_seq', (SELECT MAX(id) FROM product_image));