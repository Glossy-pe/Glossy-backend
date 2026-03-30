package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductVariantRequest {
    private Long id;  // ✅ null = nueva variante, con valor = actualizar existente
    private String toneName;
    private String toneCode;
    private BigDecimal price;
    private BigDecimal cost;
    private int stock;
    private Integer position;
    private boolean active;
    private List<VariantImageRequest> productVariantImageRequests = new ArrayList<>();
}
