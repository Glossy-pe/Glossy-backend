package com.example.repository;

import com.example.entity.SaleTemplateItem;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SaleTemplateItemRepository extends ReactiveCrudRepository<SaleTemplateItem, Long> {
    Flux<SaleTemplateItem> findByTemplateId(Long templateId);

    @Modifying
    @Query("DELETE FROM sale_template_item WHERE template_id = :templateId")
    Mono<Integer> deleteAllByTemplate(Long templateId);
}