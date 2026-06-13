package com.example.repository;

import com.example.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Flux<Order> findAllBy(Pageable pageable);
    Mono<Long> count();
}
