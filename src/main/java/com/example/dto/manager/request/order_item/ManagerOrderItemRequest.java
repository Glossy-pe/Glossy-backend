package com.example.dto.manager.request.order_item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ManagerOrderItemRequest {
    private Long productVariantId;

    private Long orderId;

    private int quantity;

    private Boolean isSeparated;

    private Boolean isPaid;

    private Boolean isPacked;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;
}
