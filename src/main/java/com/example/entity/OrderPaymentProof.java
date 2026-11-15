package com.example.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@Table("order_payment_proof")
public class OrderPaymentProof {
    @Id
    private Long id;
    private Long orderId;
    private String url;
    private String note;
    private LocalDateTime createdAt;
}