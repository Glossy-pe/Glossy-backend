package com.example.dto.admin.response.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import com.example.dto.admin.response.order_item.OrderItemResponse;

@Setter
@Getter
public class OrderResponseFull {
    private Long id;

    private String customerName;

    private String customerAddress;

    private String orderCode;

    private Long orderStatusId;

    private BigDecimal costTotal;

    private BigDecimal total;

    private List<OrderItemResponse> items;
}
