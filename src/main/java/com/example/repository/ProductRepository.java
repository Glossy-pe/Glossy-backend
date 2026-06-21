package com.example.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    // ── Manager: ve todo ──────────────────────────────
    Flux<Product> findAllBy(Pageable pageable);
    Flux<Product> findAllByIdIn(List<Long> ids);
    Flux<Product> findByNameContainingIgnoreCase(String name);
    Flux<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
    Mono<Long> countByCategoryId(Long categoryId);
    Flux<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, Long categoryId);

    // ── Guest: solo active=true y con al menos 1 variante con stock>0 ──

    @Query("""
        SELECT p.* FROM product p
        WHERE p.active = true
        AND EXISTS (
            SELECT 1 FROM product_variant pv
            WHERE pv.product_id = p.id AND pv.stock > 0 AND pv.deleted = false
        )
        ORDER BY p.id
        LIMIT :limit OFFSET :offset
        """)
    Flux<Product> findAllVisibleForGuest(int limit, long offset);

    @Query("""
        SELECT COUNT(*) FROM product p
        WHERE p.active = true
        AND EXISTS (
            SELECT 1 FROM product_variant pv
            WHERE pv.product_id = p.id AND pv.stock > 0 AND pv.deleted = false
        )
        """)
    Mono<Long> countVisibleForGuest();

    @Query("""
        SELECT p.* FROM product p
        WHERE p.active = true
        AND p.category_id = :categoryId
        AND EXISTS (
            SELECT 1 FROM product_variant pv
            WHERE pv.product_id = p.id AND pv.stock > 0 AND pv.deleted = false
        )
        ORDER BY p.id
        LIMIT :limit OFFSET :offset
        """)
    Flux<Product> findAllVisibleForGuestByCategory(Long categoryId, int limit, long offset);

    @Query("""
        SELECT COUNT(*) FROM product p
        WHERE p.active = true
        AND p.category_id = :categoryId
        AND EXISTS (
            SELECT 1 FROM product_variant pv
            WHERE pv.product_id = p.id AND pv.stock > 0 AND pv.deleted = false
        )
        """)
    Mono<Long> countVisibleForGuestByCategory(Long categoryId);

    @Query("""
        SELECT p.* FROM product p
        WHERE p.active = true
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        AND EXISTS (
            SELECT 1 FROM product_variant pv
            WHERE pv.product_id = p.id AND pv.stock > 0 AND pv.deleted = false
        )
        """)
    Flux<Product> findVisibleForGuestByNameContaining(String name);

    @Query("""
        SELECT p.* FROM product p
        WHERE p.active = true
        AND p.category_id = :categoryId
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        AND EXISTS (
            SELECT 1 FROM product_variant pv
            WHERE pv.product_id = p.id AND pv.stock > 0 AND pv.deleted = false
        )
        """)
    Flux<Product> findVisibleForGuestByNameContainingAndCategory(String name, Long categoryId);
}
