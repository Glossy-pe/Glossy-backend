package com.example.dtos.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductVariantResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String toneName;
    private String toneCode;
    private BigDecimal price;
    private BigDecimal cost;
    private Integer stock;
    private Integer position;
    private Boolean active;
    private String mainImageUrl;
    private List<VariantImageResponse> variantImageResponses = new ArrayList<>(); // 👈 nuevo campo
}