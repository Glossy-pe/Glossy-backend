package com.example.dto.admin.response.product;

import java.util.List;

import com.example.dto.admin.response.variant.VariantResponseFull;

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

    private Boolean active = true;

    private Long categoryId;

//    private String slug;

    private List<VariantResponseFull> variants;
}