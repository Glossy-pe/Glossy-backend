package com.example.dto.admin.request.order_item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    private Long productVariantId;

    private Long orderId;

    private int quantity;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;
}
