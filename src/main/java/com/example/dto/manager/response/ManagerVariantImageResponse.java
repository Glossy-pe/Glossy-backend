package com.example.dto.manager.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerVariantImageResponse {
    
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productVariantId;
}
