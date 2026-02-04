package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private String fullDescription;
    private BigDecimal basePrice;
    private boolean active = true;
    private String label;
    private Long categoryId;
}
