package com.example.dto.admin.request.product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRequest {
    private String name;

    private String description;

    private String fullDescription;

    private String label;

    private Boolean active = true;

    private Long categoryId;

//    private String slug;
}
