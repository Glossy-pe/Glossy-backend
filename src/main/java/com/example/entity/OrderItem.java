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

    private Boolean isPaid;

    private Boolean isSeparated;

    private Boolean isPacked;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;
}