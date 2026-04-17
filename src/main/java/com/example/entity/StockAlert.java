package com.example.entity;

import java.time.LocalDateTime;

import com.example.events.VariantOutOfStockEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long variantId;
    private String toneName;
    private LocalDateTime occurredAt;
    private boolean dismissed; // para el botón ✕

    public static StockAlert from(VariantOutOfStockEvent event) {
        StockAlert alert = new StockAlert();
        alert.variantId = event.getVariantId();
        alert.toneName = event.getToneName();
        alert.occurredAt = LocalDateTime.now();
        alert.dismissed = false;
        return alert;
    }

}
