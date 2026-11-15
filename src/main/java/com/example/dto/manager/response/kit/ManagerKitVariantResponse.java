package com.example.dto.manager.response.kit;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ManagerKitVariantResponse {
    private Long id;
    private String toneName;
    private String toneCode;
    private BigDecimal price;
    private Integer stock;
    private Integer position;
    private String image;       // imagen principal del tono
}