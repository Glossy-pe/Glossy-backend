package com.example.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.entity.StockAlert;
import com.example.repository.StockAlertRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockDepletedListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final StockAlertRepository stockAlertRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 👈 abre una nueva transacción
    public void onVariantOutOfStock(VariantOutOfStockEvent event) {
        boolean alreadyActive = stockAlertRepository
            .findByDismissedFalseOrderByOccurredAtDesc()
            .stream()
            .anyMatch(a -> a.getVariantId().equals(event.getVariantId()));

        if (alreadyActive) {
            System.out.println("⚠️ Ya existe alerta activa, ignorando");
            return;
        }

        StockAlert saved = stockAlertRepository.save(StockAlert.from(event));
        System.out.println("💾 Guardado en BD con id: " + saved.getId()); // ahora tendrá id

        messagingTemplate.convertAndSend("/topic/stock", saved);
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onVariantRestocked(VariantRestockedEvent event) {
        stockAlertRepository
            .findByVariantIdAndDismissedFalse(event.getVariantId())
            .ifPresent(alert -> {
                stockAlertRepository.delete(alert);
                // notifica al frontend para que quite la card
                messagingTemplate.convertAndSend("/topic/stock/restocked",
                    Map.of("variantId", event.getVariantId())
                );
            });
    }
}