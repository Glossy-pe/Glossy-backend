package com.example.dto.manager.response.images;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerVariantImageResponse {
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productVariantId;

    private String resourceType;
}
