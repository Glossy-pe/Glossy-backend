package com.example.repository;

import com.example.entity.ProductImage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductImageRepository extends ReactiveCrudRepository<ProductImage, Long> {

    Flux<ProductImage> findByProductId(Long id);
}
