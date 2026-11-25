package com.example.dto.manager.request.pucharse;
import com.example.utils.PurchaseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class ManagerPurchaseRequest {
    private Long productId;
    private PurchaseType type;
    private Integer boxUnits;          // solo BOX
    private BigDecimal totalCost;      // solo BOX
    private LocalDateTime purchaseDate; // opcional
    private String notes;
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor
    public static class Item {
        private Long variantId;
        private Integer quantity;
        private BigDecimal unitCost;   // solo UNIT
    }
}