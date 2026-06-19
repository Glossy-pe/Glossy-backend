git tag --sort=-creatordate | head -n 1


-- cart
ALTER TABLE cart
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- cart_item
ALTER TABLE cart_item
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- category
ALTER TABLE category
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- label
ALTER TABLE label
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- orders (ya tenía created_at, solo agrega las nuevas)
ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- order_item
ALTER TABLE order_item
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- product
ALTER TABLE product
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- product_image
ALTER TABLE product_image
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- product_label
ALTER TABLE product_label
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- product_variant
ALTER TABLE product_variant
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- product_variant_image
ALTER TABLE product_variant_image
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- users
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;


ALTER TABLE order_item ADD COLUMN IF NOT EXISTS paid_quantity INT NOT NULL DEFAULT 0;
ALTER TABLE order_item ADD COLUMN IF NOT EXISTS separated_quantity INT NOT NULL DEFAULT 0;
ALTER TABLE order_item ADD COLUMN IF NOT EXISTS packed_quantity INT NOT NULL DEFAULT 0;
ALTER TABLE order_item ADD COLUMN amount_paid NUMERIC(10,2);

ALTER TABLE order_item ADD COLUMN unit_price DECIMAL(10,2);

UPDATE order_item oi
SET unit_price = (
SELECT price FROM product_variant pv
WHERE pv.id = oi.product_variant_id
);


**Meilisearch integration has been removed from this branch.**


CREATE TABLE order_status (
id BIGSERIAL PRIMARY KEY,
code VARCHAR(50) NOT NULL,
description VARCHAR(255)
);

-- Insertar los estados que tenías en el enum
INSERT INTO order_status (code, description) VALUES
('PENDING', 'Pendiente'),
('CONFIRMED', 'Confirmado'),
('SHIPPED', 'Enviado'),
('DELIVERED', 'Entregado'),
('CANCELLED', 'Cancelado');

-- Agregar la columna a orders
ALTER TABLE orders ADD COLUMN order_status_id BIGINT REFERENCES order_status(id);

-- Si quieres un valor por defecto para órdenes existentes (PENDING = 1)
UPDATE orders SET order_status_id = 1 WHERE order_status_id IS NULL;


ALTER TABLE product_image
ADD COLUMN resource_type VARCHAR(10) NOT NULL DEFAULT 'image';

ALTER TABLE product_variant_image
ADD COLUMN resource_type VARCHAR(10) NOT NULL DEFAULT 'image';