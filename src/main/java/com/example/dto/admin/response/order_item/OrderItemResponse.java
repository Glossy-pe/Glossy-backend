package com.example.dto.admin.response.order_item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderItemResponse {
    private Long id;

    private Long productVariantId;

    private Long orderId;

    private int quantity;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;
}
