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
                pvi.url         AS image_url,
                pvi.main_image  AS main_image,
                pvi.position    AS image_position,
                pv.id           AS variant_id,
                pv.tone_name,
                pv.tone_code,
                pv.stock,
                pv.price
            FROM product p
            INNER JOIN product_variant pv        ON p.id = pv.product_id
            LEFT  JOIN product_variant_image pvi ON pvi.product_variant_id = pv.id
                                                AND pvi.deleted = false
            WHERE pv.id = :variantId
            LIMIT 1
            """)
        Mono<ManagerVariantQueryProjectionResponse> findVariantDetailById(Long variantId);
}