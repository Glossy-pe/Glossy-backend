package com.example.dto.manager.response.pucharse;
import com.example.utils.PurchaseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class ManagerPurchaseResponse {
    private Long id;
    private Long productId;
    private PurchaseType type;
    private Integer boxUnits;
    private BigDecimal totalCost;
    private LocalDateTime purchaseDate;
    private String notes;
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor
    public static class Item {
        private Long variantId;
        private Integer quantity;
        private BigDecimal unitCost;
        private Integer remaining;

    }
}