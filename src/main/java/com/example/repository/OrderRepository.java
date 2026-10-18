package com.example.repository;

import com.example.entity.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    @Query("""
        SELECT o.* FROM orders o
        WHERE (:name IS NULL OR LOWER(REPLACE(o.customer_name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%'))
                             OR LOWER(REPLACE(o.customer_address, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%')))
        AND (:variantId IS NULL OR EXISTS (
            SELECT 1 FROM order_item oi
            WHERE oi.order_id = o.id AND oi.product_variant_id = :variantId
        ))
        AND (:orderStatusId IS NULL OR o.order_status_id = :orderStatusId)
        AND (:isPaid IS NULL OR (
            SELECT COALESCE(SUM(oi.amount_paid), 0) >= COALESCE(SUM(COALESCE(oi.unit_price, 0) * oi.quantity), 0)
            FROM order_item oi
            WHERE oi.order_id = o.id
        ) = :isPaid)
        AND (:isPacked IS NULL OR (
            CASE WHEN :isPacked = true
                 THEN NOT EXISTS (SELECT 1 FROM order_item oi WHERE oi.order_id = o.id AND oi.is_packed IS NOT TRUE)
                 ELSE EXISTS     (SELECT 1 FROM order_item oi WHERE oi.order_id = o.id AND oi.is_packed IS NOT TRUE)
            END
        ))
        ORDER BY o.id DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<Order> findAllBy(
            @Param("name") String name,
            @Param("variantId") Long variantId,
            @Param("orderStatusId") Long orderStatusId,
            @Param("isPaid") Boolean isPaid,
            @Param("isPacked") Boolean isPacked,
            @Param("size") int size,
            @Param("offset") long offset
    );

    @Query("""
        SELECT COUNT(*) FROM orders o
        WHERE (:name IS NULL OR LOWER(REPLACE(o.customer_name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%'))
                             OR LOWER(REPLACE(o.customer_address, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%')))
        AND (:variantId IS NULL OR EXISTS (
            SELECT 1 FROM order_item oi
            WHERE oi.order_id = o.id AND oi.product_variant_id = :variantId
        ))
        AND (:orderStatusId IS NULL OR o.order_status_id = :orderStatusId)
        AND (:isPaid IS NULL OR (
            SELECT COALESCE(SUM(oi.amount_paid), 0) >= COALESCE(SUM(COALESCE(oi.unit_price, 0) * oi.quantity), 0)
            FROM order_item oi
            WHERE oi.order_id = o.id
        ) = :isPaid)
        AND (:isPacked IS NULL OR (
            CASE WHEN :isPacked = true
                 THEN NOT EXISTS (SELECT 1 FROM order_item oi WHERE oi.order_id = o.id AND oi.is_packed IS NOT TRUE)
                 ELSE EXISTS     (SELECT 1 FROM order_item oi WHERE oi.order_id = o.id AND oi.is_packed IS NOT TRUE)
            END
        ))
    """)
    Mono<Long> count(
            @Param("name") String name,
            @Param("variantId") Long variantId,
            @Param("orderStatusId") Long orderStatusId,
            @Param("isPaid") Boolean isPaid,
            @Param("isPacked") Boolean isPacked
    );

    Mono<Order> findByPublicToken(String publicToken);
}