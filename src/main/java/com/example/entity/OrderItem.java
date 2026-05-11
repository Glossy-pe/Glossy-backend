package com.example.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_item")
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    @Column(nullable = false)
    private int paidQuantity = 0;

    @Column(nullable = false)
    private int separatedQuantity = 0;

    @Column(nullable = false)
    private int packedQuantity = 0;

    @Column(nullable = true)
    private BigDecimal amountPaid;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // --- helpers paid ---

    public int getPendingQuantity() {
        return quantity - paidQuantity;
    }

    public Boolean isFullyPaid() {
        if (amountPaid == null) return false;
        return amountPaid.compareTo(getTotalPrice()) >= 0;
    }

    public BigDecimal getPendingAmount() {
        if (amountPaid == null) return getTotalPrice();
        return getTotalPrice().subtract(amountPaid);
    }

    public BigDecimal getTotalPrice() {
        return productVariant.getPrice()
            .multiply(BigDecimal.valueOf(quantity));
    }

    // --- helpers separated ---

    public int getPendingSeparatedQuantity() {
        return quantity - separatedQuantity;
    }

    public Boolean isFullySeparated() {
        return separatedQuantity >= quantity;
    }

    // --- helpers packed ---

    public int getPendingPackedQuantity() {
        return quantity - packedQuantity;
    }

    public Boolean isFullyPacked() {
        return packedQuantity >= quantity;
    }
}