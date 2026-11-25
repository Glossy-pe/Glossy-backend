package com.example.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
@Getter @Setter @NoArgsConstructor
@Table("purchase_item")
public class PurchaseItem {
    @Id
    private Long id;
    private Long purchaseId;
    private Long variantId;
    private Integer quantity;
    private BigDecimal unitCost;
    private Integer remaining = 0;
}