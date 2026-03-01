package com.example.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private UserResponse user;
    private String customerName;
    private String customerAddress;
    private String orderCode;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> orderItems = new ArrayList<>();
}
