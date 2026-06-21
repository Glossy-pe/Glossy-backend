package com.example.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository
        extends ReactiveCrudRepository<Product, Long> {
    Flux<Product> findAllBy(Pageable pageable);
    Flux<Product> findAllByIdIn(List<Long> ids);
    Flux<Product> findByNameContainingIgnoreCase(String name);

    Flux<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
    Mono<Long> countByCategoryId(Long categoryId);

    Flux<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, Long categoryId);
}
