package com.example.dto.admin.response.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderResponse {
    private Long id;

    private String customerName;

    private String customerAddress;

    private String orderCode;

    private Long orderStatusId;

    private BigDecimal costTotal;

    private BigDecimal total;
}
