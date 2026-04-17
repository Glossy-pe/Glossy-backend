package com.example.events;

import org.springframework.context.ApplicationEvent;

public class VariantRestockedEvent extends ApplicationEvent {

    private final Long variantId;

    public VariantRestockedEvent(Object source, Long variantId) {
        super(source);
        this.variantId = variantId;
    }

    public Long getVariantId() { return variantId; }
}