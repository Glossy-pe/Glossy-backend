package com.example.entity;

import com.example.utils.PurchaseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table("purchase")
public class Purchase {
    @Id
    private Long id;
    private Long productId;
    private PurchaseType type;
    private Integer boxUnits;
    private BigDecimal totalCost;
    private LocalDateTime purchaseDate;
    private String notes;
}