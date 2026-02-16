package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequestV2 {
    private String name;
    private String description;
    private String fullDescription;
    private boolean active = true;

    private Long categoryId;
    private List<ProductImageRequest> images;
    private List<ProductVariantRequest> variants;
    private List<Long> labelsIds;
}
