package com.example.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantImageRequest {

    @NotNull
    private Long variantId;

    @NotBlank
    private String url;

    @Min(1)
    private Integer position;

    private boolean mainImage;
}