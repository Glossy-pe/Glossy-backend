package com.example.repository;

import com.example.entity.Kit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface KitRepository extends ReactiveCrudRepository<Kit, Long> {

    Flux<Kit> findAllByActiveTrue();
}