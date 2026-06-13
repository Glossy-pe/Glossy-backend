package com.example.dto.admin.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantImageResponse {
    
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productVariantId;
}
