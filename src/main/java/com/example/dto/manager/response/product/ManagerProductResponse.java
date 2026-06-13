package com.example.dto.manager.response.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerProductResponse {
    
    private Long id;

    private String name;

    private String description;

    private String fullDescription;

    private String label;

    private Boolean active = true;

    private Long categoryId;

//    private String slug;
}
