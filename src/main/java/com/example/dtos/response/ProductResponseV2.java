package com.example.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponseV2 {
    private Long id;
    private String name;
    private String description;
    private String fullDescription;
    private boolean active = true;

    private Long categoryId;
    private List<ProductImageResponse> images;
    private List<ProductVariantResponse> variants;
    private List<LabelResponse> labels;
}
