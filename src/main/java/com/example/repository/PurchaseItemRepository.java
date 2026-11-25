package com.example.repository;
import com.example.entity.Purchase;
import com.example.entity.PurchaseItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PurchaseItemRepository extends ReactiveCrudRepository<PurchaseItem, Long> {
    Flux<PurchaseItem> findByPurchaseId(Long purchaseId);
}