package com.example.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.entity.ProductVariant;

import jakarta.persistence.PreUpdate;

@Component
public class VariantEntityListener {

    // truco para acceder al contexto de Spring desde un JPA listener
    private static ApplicationEventPublisher eventPublisher;

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher publisher) {
        VariantEntityListener.eventPublisher = publisher;
    }

    @PreUpdate
    public void preUpdate(ProductVariant variant) {
        if (variant.getStock() <= 0) {
            eventPublisher.publishEvent(new VariantOutOfStockEvent(
                this,
                variant.getId(),
                variant.getToneName() != null ? variant.getToneName() : "Sin nombre"
            ));
        } else if (variant.getStock() > 0) {
            // 👈 stock restaurado
            eventPublisher.publishEvent(
                new VariantRestockedEvent(this, variant.getId())
            );
        }
    }
}
