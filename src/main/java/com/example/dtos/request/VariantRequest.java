package com.example.dtos.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VariantRequest {

    @NotNull
    private Long productId;

    @NotBlank
    private String toneName;

    @NotBlank
    private String toneCode;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    @DecimalMin("0.00")
    private BigDecimal cost;

    @Min(0)
    private int stock;

    @Min(1)
    private Integer position;

    private boolean active = true;
}
