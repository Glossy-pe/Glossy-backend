package com.example.dto.manager.request.images;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerProductImageRequest {
    private String url;

    private int position;

    private Boolean mainImage;

    private Long productId;

    private String resourceType;
}
