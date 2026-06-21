package com.example.dto.manager.response.product;

import java.util.List;

import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.variant.ManagerVariantResponseFull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerProductResponseFull {

    private Long id;

    private String name;

    private String description;

    private String fullDescription;

    private String label;

    private Boolean active = true;

    private Long categoryId;

//    private String slug;
    private List<ManagerProductImageResponse> images;

    private List<ManagerVariantResponseFull> variants;
}