package com.example.repository;

import com.example.entity.Product;
import com.example.repository.projection.StockAlertProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ── Con soft delete ──────────────────────────────────────────────

    Optional<Product> findByIdAndDeletedFalse(Long id);

    Page<Product> findAllByDeletedFalse(Pageable pageable);

    List<Product> findAllByDeletedFalse();

    Optional<Product> findBySlugAndDeletedFalse(String slug);

    Page<Product> findByCategoryIdAndDeletedFalse(Long categoryId, Pageable pageable);

    Page<Product> findByProductLabels_Label_IdAndDeletedFalse(Long labelId, Pageable pageable);

    // ── Existentes (sin cambios) ─────────────────────────────────────

    List<Product> findByLabel(String label);

    @Query(value = """
        SELECT p.*
        FROM product p
        INNER JOIN product_label pl ON p.id = pl.product_id
        WHERE pl.label_id = :labelId
    """, nativeQuery = true)
    List<Product> findByLabelId(@Param("labelId") Long labelId);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByProductLabels_Label_Id(Long labelId, Pageable pageable);

    Optional<Product> findBySlug(String slug);


@Query(value = """
    SELECT
        p.id                                            AS productId,
        p.name                                          AS productName,
        p.slug                                          AS productSlug,
        pv.id                                           AS variantId,
        pv.tone_name                                    AS toneName,
        pv.tone_code                                    AS toneCode,
        pv.stock                                        AS variantStock,
        COALESCE(recent7.sold, 0)                       AS variantRecentSales,
        COALESCE(recent1.sold, 0)                       AS variantHotSales,
        (
            SELECT pvi.url
            FROM product_variant_image pvi
            INNER JOIN product_variant pv2
                ON pvi.product_variant_id = pv2.id
            WHERE pv2.product_id = p.id
              AND pv2.deleted IS DISTINCT FROM true
              AND pvi.main_image = true
            LIMIT 1
        )                                               AS mainImageUrl

    FROM product p
    INNER JOIN product_variant pv
        ON pv.product_id = p.id
        AND pv.deleted IS DISTINCT FROM true
        AND pv.stock < 2
    LEFT JOIN (
        SELECT oi.product_variant_id, SUM(oi.quantity) AS sold
        FROM order_item oi
        INNER JOIN orders o ON o.id = oi.order_id
        WHERE o.created_at >= NOW() - INTERVAL '7 days'
        GROUP BY oi.product_variant_id
    ) recent7 ON recent7.product_variant_id = pv.id
    LEFT JOIN (
        SELECT oi.product_variant_id, SUM(oi.quantity) AS sold
        FROM order_item oi
        INNER JOIN orders o ON o.id = oi.order_id
        WHERE o.created_at >= NOW() - INTERVAL '1 day'
        GROUP BY oi.product_variant_id
    ) recent1 ON recent1.product_variant_id = pv.id

    WHERE p.deleted = false
      AND p.active = true

    ORDER BY p.id ASC
""", nativeQuery = true)
List<StockAlertProjection> findStockAlerts();
}