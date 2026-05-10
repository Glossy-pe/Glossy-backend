package com.example.dtos.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long productVariantId;
    private int quantity;
    private int paidQuantity;
    private BigDecimal amountPaid;
    private int separatedQuantity;
    private int packedQuantity;
}
