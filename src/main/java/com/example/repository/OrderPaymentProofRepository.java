package com.example.repository;

import com.example.entity.OrderPaymentProof;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderPaymentProofRepository extends ReactiveCrudRepository<OrderPaymentProof, Long> {
    Flux<OrderPaymentProof> findByOrderIdOrderByCreatedAtDesc(Long orderId);
}