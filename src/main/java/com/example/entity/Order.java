package com.example.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String description;

    private String orderCode;

    private Long orderStatusId;

    private BigDecimal costTotal;

    private BigDecimal total;

    private String publicToken;

    private LocalDateTime expiresAt;
}