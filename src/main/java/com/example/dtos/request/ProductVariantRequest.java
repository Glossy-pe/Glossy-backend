package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantRequest {
    private Long id;  // ✅ null = nueva variante, con valor = actualizar existente
    private String toneName;
    private String toneCode;
    private BigDecimal price;
    private int stock;
    private boolean active;
}
