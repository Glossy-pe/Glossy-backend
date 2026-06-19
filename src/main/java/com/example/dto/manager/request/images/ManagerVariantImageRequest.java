package com.example.dto.manager.request.images;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerVariantImageRequest {
    private String url;

    private int position;

    private Boolean mainImage;

    private Long productVariantId;

    private String resourceType;
}
