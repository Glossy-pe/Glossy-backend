package com.example.events;

import org.springframework.context.ApplicationEvent;

public class VariantOutOfStockEvent extends ApplicationEvent {

    private final Long variantId;
    private final String toneName;

    public VariantOutOfStockEvent(Object source, Long variantId, String toneName) {
        super(source);
        this.variantId = variantId;
        this.toneName = toneName;
    }

    public Long getVariantId() { return variantId; }
    public String getToneName() { return toneName; }
}
