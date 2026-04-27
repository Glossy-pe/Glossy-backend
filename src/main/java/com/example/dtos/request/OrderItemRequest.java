package com.example.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long productVariantId;
    private int quantity;
    private Boolean separated;
    private Boolean packed;
}
