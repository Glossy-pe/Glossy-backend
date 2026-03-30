package com.example.dtos.response.full;

import java.util.List;

import com.example.dtos.response.LabelResponse;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductResponseFull {
    private Long id;
    private String name;
    private String description;
    private String fullDescription;
    private String label;
    private Boolean active;

    private Long categoryId;

    private List<VariantResponseFull> variants;
    private List<LabelResponse> labels;
}
