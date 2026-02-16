package com.example.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String fullDescription;
    private List<ProductImageResponse> images= new ArrayList<>();
    private boolean active = true;
    private String label;
    private Long categoryId;
    private List<ProductVariantResponse> variants = new ArrayList<>();

//    private List<LabelResponse> labels;
}
