package com.example.dto.manager.response.order;

import com.example.dto.manager.response.auditable.AuditableResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ManagerOrderResponse extends AuditableResponse {
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
