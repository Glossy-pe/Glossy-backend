package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Table("order_item")
@NoArgsConstructor
public class OrderItem extends Auditable{

    @Id
    private Long id;

    private Long productVariantId;

    private Long orderId;

    private int quantity;

    private int paidQuantity = 0;

    private int separatedQuantity = 0;

    private int packedQuantity = 0;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;

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
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
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