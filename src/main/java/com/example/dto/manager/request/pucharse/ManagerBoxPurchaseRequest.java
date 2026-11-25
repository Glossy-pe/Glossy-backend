package com.example.dto.manager.request.pucharse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class ManagerBoxPurchaseRequest {
    private Long productId;
    private Integer boxCount;          // cuántos BOX llegaron
    private BigDecimal costPerBox;
    private LocalDateTime purchaseDate; // opcional
    private String notes;
}