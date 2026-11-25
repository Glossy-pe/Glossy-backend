    git tag --sort=-creatordate | head -n 1

-- Lo que queda de cada variante dentro de un BOX (solo aplica a compras BOX)
ALTER TABLE purchase_item ADD COLUMN remaining INT NOT NULL DEFAULT 0;
ALTER TABLE purchase_item ADD CONSTRAINT chk_purchase_item_remaining
CHECK (remaining >= 0 AND remaining <= quantity);

-- Para la futura venta: buscar rápido qué BOX tienen unidades de una variante
CREATE INDEX idx_purchase_item_remaining ON purchase_item (variant_id) WHERE remaining > 0;

CREATE TABLE box_presentation (
id          BIGSERIAL PRIMARY KEY,
product_id  BIGINT    NOT NULL UNIQUE REFERENCES product(id),
total_units INT       NOT NULL CHECK (total_units > 0),
created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE sale_template (
id         BIGSERIAL PRIMARY KEY,
product_id BIGINT        NOT NULL REFERENCES product(id),
name       VARCHAR(60)   NOT NULL,
price      NUMERIC(12,2) NOT NULL CHECK (price >= 0),
active     BOOLEAN       NOT NULL DEFAULT true,
created_at TIMESTAMP     NOT NULL DEFAULT now(),
CONSTRAINT uq_sale_template UNIQUE (product_id, name)
);

CREATE TABLE sale_template_item (
id          BIGSERIAL PRIMARY KEY,
template_id BIGINT NOT NULL REFERENCES sale_template(id) ON DELETE CASCADE,
variant_id  BIGINT NOT NULL REFERENCES product_variant(id),
quantity    INT    NOT NULL CHECK (quantity > 0),
CONSTRAINT uq_sale_template_item UNIQUE (template_id, variant_id)
);