package com.example.dto.guest.response.image;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GuestProductImageResponse {
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productId;

    private String resourceType;
}
