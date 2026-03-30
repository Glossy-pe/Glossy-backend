package com.example.dtos.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String toneName;
    private String toneCode;
    private BigDecimal price;
    private BigDecimal cost;
    private int stock;
    private Integer position;
    private boolean active;
}
