package com.example.dto.manager.request.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ManagerOrderRequest {

    private String customerName;

    private String customerAddress;

    private String description;

    private LocalDateTime expiresAt;

//    private String orderCode;

    private Long orderStatusId;

//    private BigDecimal costTotal;

//    private BigDecimal total;
}
