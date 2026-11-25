package com.example.repository;

import com.example.entity.SaleTemplate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SaleTemplateRepository extends ReactiveCrudRepository<SaleTemplate, Long> {
    Flux<SaleTemplate> findByProductIdOrderByIdAsc(Long productId);
}
