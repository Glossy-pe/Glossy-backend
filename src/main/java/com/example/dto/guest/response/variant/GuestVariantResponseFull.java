package com.example.dto.guest.response.variant;

import com.example.dto.guest.response.image.GuestVariantImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GuestVariantResponseFull {
    private Long id;

    private String toneName;

    private String toneCode;

    private BigDecimal cost;

    private BigDecimal price;

    private int stock;

    private Integer position;

    private Boolean active = true;

    private Long productId;

    private List<GuestVariantImageResponse> images;
}
