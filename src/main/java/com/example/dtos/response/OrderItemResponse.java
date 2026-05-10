package com.example.dtos.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long id;
    private ProductVariantResponse productVariant;
    private int quantity;
    private int paidQuantity;
    private BigDecimal amountPaid;
    private int separatedQuantity;
    private int packedQuantity;
}
