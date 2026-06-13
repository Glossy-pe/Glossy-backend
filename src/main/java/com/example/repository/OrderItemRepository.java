package com.example.repository;

import com.example.entity.OrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {
    Flux<OrderItem> findByOrderId(Long orderId);
    Mono<Void> deleteAllByOrderId(Long orderId);
    Mono<OrderItem> findByOrderIdAndProductVariantId(Long orderId, Long productVariantId);

}
