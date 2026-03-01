package com.example.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String toneName;   // Claro, Medio, Oscuro
    private String toneCode;   // CL-01, MD-02
    private BigDecimal price;
    private int stock;
}
