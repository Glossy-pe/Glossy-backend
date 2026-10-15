package com.example.dto.manager.response.order_item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ManagerOrderItemResponseStructured {
    private Long id;
    private Long orderId;
    private Long productVariantId;
    private String productName;
    private String toneName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal definedPrice;
    private BigDecimal amountPaid;
    private Boolean isSeparated;
    private Boolean isPacked;
    private String image;
}
