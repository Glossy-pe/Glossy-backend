package com.example.repository;

import com.example.entity.Order;
import org.springframework.data.domain.Pageable;
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
            SELECT COUNT(*) FILTER (WHERE oi.paid_quantity < oi.quantity) FROM order_item oi WHERE oi.order_id = o.id
        ) = 0)
        AND (:isSeparated IS NULL OR (
            SELECT COUNT(*) FILTER (WHERE oi.separated_quantity < oi.quantity) FROM order_item oi WHERE oi.order_id = o.id
        ) = 0)
        AND (:isPacked IS NULL OR (
            SELECT COUNT(*) FILTER (WHERE oi.packed_quantity < oi.quantity) FROM order_item oi WHERE oi.order_id = o.id
        ) = 0)
        ORDER BY o.id DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<Order> findAllBy(
            @Param("name") String name,
            @Param("variantId") Long variantId,
            @Param("orderStatusId") Long orderStatusId,
            @Param("isPaid") Boolean isPaid,
            @Param("isSeparated") Boolean isSeparated,
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
            SELECT COUNT(*) FILTER (WHERE oi.paid_quantity < oi.quantity) FROM order_item oi WHERE oi.order_id = o.id
        ) = 0)
        AND (:isSeparated IS NULL OR (
            SELECT COUNT(*) FILTER (WHERE oi.separated_quantity < oi.quantity) FROM order_item oi WHERE oi.order_id = o.id
        ) = 0)
        AND (:isPacked IS NULL OR (
            SELECT COUNT(*) FILTER (WHERE oi.packed_quantity < oi.quantity) FROM order_item oi WHERE oi.order_id = o.id
        ) = 0)
    """)
    Mono<Long> count(
            @Param("name") String name,
            @Param("variantId") Long variantId,
            @Param("orderStatusId") Long orderStatusId,
            @Param("isPaid") Boolean isPaid,
            @Param("isSeparated") Boolean isSeparated,
            @Param("isPacked") Boolean isPacked
    );
}