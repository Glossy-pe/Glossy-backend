package com.example.dtos.response.full;
import java.math.BigDecimal;
import java.util.List;

import com.example.dtos.response.VariantImageResponse;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class VariantResponseFull {
    private Long id;
    private String toneName;
    private String toneCode;
    private BigDecimal cost;
    private BigDecimal price;
    private Integer stock;
    private Integer position;
    private Boolean active;

    private List<VariantImageResponse> images;
}
