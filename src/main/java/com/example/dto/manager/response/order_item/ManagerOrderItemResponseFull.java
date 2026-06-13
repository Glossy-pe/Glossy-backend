package com.example.dto.manager.response.order_item;

import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import com.example.dto.manager.response.variant.ManagerVariantResponseFull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ManagerOrderItemResponseFull {
    private Long id;

    private Long productVariantId;

    private Long orderId;

    private int quantity;

    private int paidQuantity = 0;

    private int separatedQuantity = 0;

    private int packedQuantity = 0;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;

    private ManagerVariantQueryProjectionResponse variant;
}
