package com.example.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long id;
    private ProductVariantResponse productVariant;
    private int quantity;
    private Boolean separated;
    private Boolean packed;
}
