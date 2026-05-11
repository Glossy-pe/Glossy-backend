package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {
    private String url;
    private int position;
    private Boolean mainImage;
}
