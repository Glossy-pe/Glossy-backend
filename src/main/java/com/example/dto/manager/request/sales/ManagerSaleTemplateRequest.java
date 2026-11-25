package com.example.dto.manager.request.sales;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class ManagerSaleTemplateRequest {
    private Long productId;     // solo al crear
    private String name;
    private BigDecimal unitPrice;
    private Boolean active;     // opcional
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor
    public static class Item {
        private Long variantId;
        private Integer quantity;
    }
}