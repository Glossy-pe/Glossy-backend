package com.example.dto.manager.response.order;

import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.dto.manager.response.auditable.AuditableResponse;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class ManagerOrderResponseFull extends AuditableResponse {
    private Long id;

    private String customerName;

    private String customerAddress;

    private String orderCode;

    private Long orderStatusId;

    private BigDecimal costTotal;

    private BigDecimal total;

    private List<ManagerOrderItemResponseFull> items;
}
