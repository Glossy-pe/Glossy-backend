package com.example.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
@Getter @Setter @NoArgsConstructor
@Table("sale_template")
public class SaleTemplate {
    @Id
    private Long id;
    private Long productId;
    private String name;
    private BigDecimal unitPrice;
    private Boolean active = true;
}