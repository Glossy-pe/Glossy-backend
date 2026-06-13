package com.example.dto.manager.request.variant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ManagerVariantRequest {
    private String toneName;

    private String toneCode;

    private BigDecimal cost;

    private BigDecimal price;

    private int stock;

    private Integer position;

    private Boolean active = true;

    private Long productId;
}
