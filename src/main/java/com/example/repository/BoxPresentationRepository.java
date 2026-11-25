package com.example.repository;

import com.example.entity.BoxPresentation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BoxPresentationRepository extends ReactiveCrudRepository<BoxPresentation, Long> {
    Mono<BoxPresentation> findByProductId(Long productId);
}