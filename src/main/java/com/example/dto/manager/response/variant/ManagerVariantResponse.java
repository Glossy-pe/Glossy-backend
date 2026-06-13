package com.example.dto.manager.response.variant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ManagerVariantResponse {
    private Long id;

    private String toneName;

    private String toneCode;

    private BigDecimal cost;

    private BigDecimal price;

    private int stock;

    private Integer position;

    private Boolean active = true;

    private Long productId;
}
