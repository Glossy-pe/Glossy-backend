package com.example.dto.manager.response.variant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ManagerVariantQueryProjectionResponse {

    private Long productId;
    private String productName;
    private Long variantId;
    private String toneName;
    private String toneCode;
    private Integer stock;
    private BigDecimal price;
    private String imageUrl;
    private Boolean mainImage;
    private Integer imagePosition;
}