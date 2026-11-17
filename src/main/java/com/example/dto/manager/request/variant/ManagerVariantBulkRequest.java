package com.example.dto.manager.request.variant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ManagerVariantBulkRequest {
    private Long productId;
    private Integer count;
    private BigDecimal cost;
    private BigDecimal price;
    private Integer stock;
    private String toneCode = "#000000";
}