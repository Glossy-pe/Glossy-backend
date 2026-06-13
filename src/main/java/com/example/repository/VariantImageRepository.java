package com.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.entity.ProductVariantImage;

import reactor.core.publisher.Flux;

public interface VariantImageRepository extends ReactiveCrudRepository<ProductVariantImage, Long> {
    Flux<ProductVariantImage> findByProductVariantId(Long productVariantId);
}
