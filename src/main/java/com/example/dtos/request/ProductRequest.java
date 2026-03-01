package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private String fullDescription;
    private boolean active = true;
    private String label;
    private Long categoryId;
//    private List<Long> labelIds;
}
