package com.example.repository;

import com.example.entity.Purchase;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PurchaseRepository extends ReactiveCrudRepository<Purchase, Long> {
    Flux<Purchase> findByProductIdOrderByPurchaseDateDescIdDesc(Long productId);
}