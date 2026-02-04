package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantRequest {
    private String toneName;   // Claro, Medio, Oscuro
    private String toneCode;   // CL-01, MD-02
    private BigDecimal price;
    private int stock;
}
