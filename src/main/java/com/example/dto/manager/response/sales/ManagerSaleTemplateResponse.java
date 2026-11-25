package com.example.dto.manager.response.sales;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter @Setter @NoArgsConstructor
public class ManagerSaleTemplateResponse {
    private Long id;
    private Long productId;
    private String name;
    private BigDecimal unitPrice;        // lo que defines por unidad
    private BigDecimal totalPrice;       // unitPrice × unidades de la plantilla
    private BigDecimal referencePrice;   // lo que costaría vendido por unidad normal
    private BigDecimal discount;         // referencePrice - totalPrice
    private BigDecimal discountPercent;
    private Boolean active;
    private int totalUnits;
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor
    public static class Item {
        private Long variantId;
        private String toneName;
        private Integer quantity;
        private BigDecimal unitPrice;    // ProductVariant.price actual
    }
}