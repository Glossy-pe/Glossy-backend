package com.example.dtos.request.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private String fullDescription;
    private Boolean active = true;
    private Long categoryId;
}
