package com.example.repository;

import com.example.dto.manager.response.order_item.ManagerOrderItemResponseStructured;
import com.example.dto.manager.response.product.ManagerProductOrderRow;
import com.example.dto.manager.response.product.ManagerProductOrderVariantResponse;
import com.example.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {
    Flux<OrderItem> findAllBy(Pageable pageable);
    Flux<OrderItem> findByOrderId(Long orderId);
    Mono<Void> deleteAllByOrderId(Long orderId);
    Mono<OrderItem> findByOrderIdAndProductVariantId(Long orderId, Long productVariantId);
    Flux<OrderItem> findByOrderId(Long orderId, Pageable pageable);
    Mono<Long> countByOrderId(Long orderId);

    @Query("""
        SELECT
            oi.id          AS id,
            oi.order_id          AS order_id,
            pv.id          AS product_variant_id,
            p.name          AS product_name,
            t.url           AS image,
            pv.tone_name    AS tone_name,
            pv.price        AS price,
            oi.unit_price   AS defined_price,
            oi.quantity     AS quantity,
            oi.amount_paid  AS amount_paid,
            oi.is_separated  AS is_separated,
            oi.is_packed  AS is_packed
        FROM order_item oi
        INNER JOIN product_variant pv ON oi.product_variant_id = pv.id
        INNER JOIN product p ON p.id = pv.product_id
        LEFT JOIN product_image t
            ON t.product_id = p.id AND t.main_image = true
        WHERE oi.order_id = :orderId
        """)
    Flux<ManagerOrderItemResponseStructured> findStructuredByOrderId(Long orderId);

    @Query("""
    SELECT o.id AS order_id, o.order_code, o.customer_name, o.customer_address,
           o.order_status_id, o.created_at
    FROM orders o
    WHERE EXISTS (
        SELECT 1 FROM order_item oi
        INNER JOIN product_variant pv ON pv.id = oi.product_variant_id
        WHERE oi.order_id = o.id AND pv.product_id = :productId
    )
    AND (:name IS NULL OR LOWER(regexp_replace(o.customer_name, '[[:space:]' || chr(160) || chr(8199) || chr(8239) || chr(8203) || ']', '', 'g')) LIKE CONCAT('%', :name, '%'))
    AND (:orderStatusId IS NULL OR o.order_status_id = :orderStatusId)
    ORDER BY o.id DESC
    LIMIT :size OFFSET :offset
    """)
    Flux<ManagerProductOrderRow> findOrdersByProduct(
            @Param("productId") Long productId,
            @Param("name") String name,
            @Param("orderStatusId") Long orderStatusId,
            @Param("size") int size,
            @Param("offset") long offset);

    @Query("""
    SELECT COUNT(*) FROM orders o
    WHERE EXISTS (
        SELECT 1 FROM order_item oi
        INNER JOIN product_variant pv ON pv.id = oi.product_variant_id
        WHERE oi.order_id = o.id AND pv.product_id = :productId
    )
    AND (:name IS NULL OR LOWER(regexp_replace(o.customer_name, '[[:space:]' || chr(160) || chr(8199) || chr(8239) || chr(8203) || ']', '', 'g')) LIKE CONCAT('%', :name, '%'))
    AND (:orderStatusId IS NULL OR o.order_status_id = :orderStatusId)
    """)
    Mono<Long> countOrdersByProduct(
            @Param("productId") Long productId,
            @Param("name") String name,
            @Param("orderStatusId") Long orderStatusId);

    @Query("""
    SELECT oi.id AS order_item_id, oi.order_id, pv.id AS variant_id,
           pv.tone_name, pv.tone_code,
           oi.unit_price, oi.amount_paid, oi.is_separated, oi.is_packed
    FROM order_item oi
    INNER JOIN product_variant pv ON pv.id = oi.product_variant_id
    WHERE pv.product_id = :productId AND oi.order_id IN (:orderIds)
    ORDER BY oi.order_id DESC, pv.position, oi.id
    """)
    Flux<ManagerProductOrderVariantResponse> findProductItemsByOrderIds(
            @Param("productId") Long productId,
            @Param("orderIds") List<Long> orderIds);
}
