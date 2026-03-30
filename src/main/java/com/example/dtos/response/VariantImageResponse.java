package com.example.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantImageResponse {
    private Long id;
    private Long variantId;
    private String url;
    private Integer position;
    private boolean mainImage;
}
