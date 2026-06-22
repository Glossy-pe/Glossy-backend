package com.example.dto.manager.response.order;

import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.dto.manager.response.auditable.AuditableResponse;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import com.example.dto.manager.response.order_status.ManagerOrderStatusResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ManagerOrderResponseFull extends AuditableResponse {
    private Long id;

    private String customerName;

    private String customerAddress;

    private String description;

    private String orderCode;

    private String publicToken;

    private LocalDateTime expiresAt;

    private Long orderStatusId;

    private ManagerOrderStatusResponse orderStatus;

    private Boolean packed;

    private Boolean separated;

    private Boolean paid;

    private BigDecimal costTotal;

    private BigDecimal total;

    private List<ManagerOrderItemResponseFull> items;
}
