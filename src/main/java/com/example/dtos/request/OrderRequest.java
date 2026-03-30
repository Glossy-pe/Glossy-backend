package com.example.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private OrderStatus status;
    private BigDecimal total;
    private BigDecimal costTotal;
    private String customerName;
    private String customerAddress;
    private LocalDateTime createdAt;
    private List<OrderItemRequest> orderItems = new ArrayList<>();
}
