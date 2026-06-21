package com.example.repository;

import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import com.example.entity.ProductVariant;

import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface VariantRepository extends ReactiveCrudRepository<ProductVariant, Long> {
        Flux<ProductVariant> findByProductId(Long productId);


        @Query("""
    SELECT
        p.id            AS product_id,
        p.name          AS product_name,
        pi.url          AS image_url,
        pi.main_image   AS main_image,
        pi.position     AS image_position,
        pv.id           AS variant_id,
        pv.tone_name,
        pv.tone_code,
        pv.stock,
        pv.price
    FROM product p
    INNER JOIN product_variant pv  ON p.id = pv.product_id
    LEFT  JOIN LATERAL (
        SELECT url, main_image, position
        FROM product_image
        WHERE product_id = p.id
          AND (resource_type IS DISTINCT FROM 'video')
        ORDER BY main_image DESC NULLS LAST, position ASC
        LIMIT 1
    ) pi ON true
    WHERE pv.id = :variantId
    LIMIT 1
    """)
        Mono<ManagerVariantQueryProjectionResponse> findVariantDetailById(Long variantId);
}