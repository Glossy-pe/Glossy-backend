package com.example.repository;

import com.example.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {
    Flux<OrderItem> findAllBy(Pageable pageable);
    Flux<OrderItem> findByOrderId(Long orderId);
    Mono<Void> deleteAllByOrderId(Long orderId);
    Mono<OrderItem> findByOrderIdAndProductVariantId(Long orderId, Long productVariantId);
    Flux<OrderItem> findByOrderId(Long orderId, Pageable pageable);
    Mono<Long> countByOrderId(Long orderId);



//    @Query("""
//    SELECT DISTINCT
//        p.id           AS product_id,
//        p.name         AS product_name,
//        pi.url         AS image_url
//    FROM order_item oi
//    INNER JOIN product_variant pv ON oi.product_variant_id = pv.id
//    INNER JOIN product p ON pv.product_id = p.id
//    LEFT JOIN LATERAL (
//        SELECT url FROM product_image
//        WHERE product_id = p.id
//          AND (resource_type IS DISTINCT FROM 'video')
//        ORDER BY main_image DESC NULLS LAST, position ASC
//        LIMIT 1
//    ) pi ON true
//    ORDER BY p.name ASC
//""")
//    Flux<ManagerOrderItemProductProjection> findDistinctProducts();
//
//    @Query("""
//    SELECT DISTINCT
//        pv.id        AS variant_id,
//        pv.tone_name AS tone_name,
//        pv.tone_code AS tone_code
//    FROM order_item oi
//    INNER JOIN product_variant pv ON oi.product_variant_id = pv.id
//    WHERE pv.product_id = :productId
//    ORDER BY pv.tone_name ASC
//""")
//    Flux<ManagerOrderItemVariantProjection> findDistinctVariantsByProductId(Long productId);
}
