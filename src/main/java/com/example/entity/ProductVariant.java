package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Table("product_variant")
public class ProductVariant extends SoftDeletable {

    @Id
    private Long id;

    private String toneName;

    private String toneCode;

    private BigDecimal cost;

    private BigDecimal price;

    private int stock;

    private Integer position;

    private Boolean active = true;

    private Long productId;
}