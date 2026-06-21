package com.example.repository;

import com.example.entity.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BrandRepository
        extends ReactiveCrudRepository<Brand, Long> {

    Mono<Brand> findBySlug(String slug);

    Flux<Brand> findAllByActiveTrue();
}