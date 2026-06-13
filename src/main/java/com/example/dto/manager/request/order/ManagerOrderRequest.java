package com.example.dto.manager.request.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ManagerOrderRequest {

    private String customerName;

    private String customerAddress;

//    private String orderCode;

    private Long orderStatusId;

//    private BigDecimal costTotal;

//    private BigDecimal total;
}
