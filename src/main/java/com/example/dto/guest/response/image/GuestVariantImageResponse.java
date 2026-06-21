package com.example.dto.guest.response.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestVariantImageResponse {
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productVariantId;

    private String resourceType;
}
