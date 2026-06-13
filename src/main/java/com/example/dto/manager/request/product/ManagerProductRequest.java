package com.example.dto.manager.request.product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerProductRequest {
    private String name;

    private String description;

    private String fullDescription;

    private String label;

    private Boolean active = true;

    private Long categoryId;

//    private String slug;
}
