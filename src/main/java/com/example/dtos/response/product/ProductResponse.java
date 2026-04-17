package com.example.dtos.response.product;

import java.util.List;

import com.example.dtos.response.LabelResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String fullDescription;
    private Boolean active = true;
    private Long categoryId;
    private List<LabelResponse> labels;
}
