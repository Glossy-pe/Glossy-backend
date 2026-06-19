package com.example.dto.manager.response.images;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
public class ManagerProductImageResponse {
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productId;

    private String resourceType;
}
