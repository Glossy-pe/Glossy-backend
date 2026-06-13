package com.example.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order extends Auditable {

    @Id
    private Long id;

    private String customerName;

    private String customerAddress;

    private String orderCode;

    private Long orderStatusId;

    private BigDecimal costTotal;

    private BigDecimal total;
}