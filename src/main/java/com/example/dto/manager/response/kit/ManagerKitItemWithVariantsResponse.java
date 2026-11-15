package com.example.dto.manager.response.kit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ManagerKitItemWithVariantsResponse {
    private Long itemId;
    private Integer position;
    private Long productId;
    private String productName;
    private String image;       // imagen principal del producto
    private List<ManagerKitVariantResponse> variants;
}