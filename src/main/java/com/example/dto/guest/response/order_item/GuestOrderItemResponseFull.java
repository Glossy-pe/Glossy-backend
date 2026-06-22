package com.example.dto.guest.response.order_item;

import com.example.dto.guest.response.variant.GuestVariantResponseFull;
import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class GuestOrderItemResponseFull {
    private Long id;

    private Long productVariantId;

    private Long orderId;

    private int quantity;

    private int paidQuantity = 0;

    private int separatedQuantity = 0;

    private int packedQuantity = 0;

    private BigDecimal amountPaid;

    private BigDecimal unitPrice;

    private GuestVariantResponseFull variant;
}
