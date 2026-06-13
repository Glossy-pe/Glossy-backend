package com.example.dto.manager.response.variant;

import java.math.BigDecimal;
import java.util.List;

import com.example.dto.manager.response.ManagerVariantImageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerVariantResponseFull {
    
    private Long id;

    private String toneName;

    private String toneCode;

    private BigDecimal cost;

    private BigDecimal price;

    private int stock;

    private Integer position;

    private Boolean active = true;

    private Long productId;

    private List<ManagerVariantImageResponse> images;
}
