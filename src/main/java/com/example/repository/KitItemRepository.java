package com.example.repository;

import com.example.entity.KitItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KitItemRepository extends ReactiveCrudRepository<KitItem, Long> {

    Flux<KitItem> findByKitIdOrderByPosition(Long kitId);

    Mono<Boolean> existsByKitIdAndPosition(Long kitId, Integer position);

    Mono<Boolean> existsByKitIdAndProductId(Long kitId, Long productId);

    Mono<Void> deleteByKitId(Long kitId);
}